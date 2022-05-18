/*
 * IPK - 2. projekt Varianta ZETA: Sniffer paketů
 * @author Vojtěch Hájek (xhajek51)
 */
#include <stdio.h>
#include <stdlib.h>
#include <getopt.h>
#include <stdbool.h>
#include <string.h>
#include <signal.h>
#include <pcap.h>
#include <time.h>
#include <arpa/inet.h>
#include <netinet/if_ether.h>
#include <netinet/ip.h>
#include <netinet/udp.h>
#include <netinet/ip6.h>
#include <netinet/tcp.h>


// macro to set optional argument
#define OPTIONAL_ARGUMENT_IS_PRESENT \
    ((optarg == NULL && optind < argc && argv[optind][0] != '-') \
     ? (bool) (optarg = argv[optind++]) \
     : (optarg != NULL))

// global variables
char expression[256];
pcap_t *handle;

/**
 * @brief function to end program when user press signal CTRL+C
 * @param s signal
 */
void my_handler(sig_atomic_t s) {
    pcap_close(handle);
    exit(0);
}

/**
 * @brief function to get filter expression string
 * @param port_flag entered / not entered port
 * @param port port number
 * @param tcp_flag entered / not entered tcp argument
 * @param udp_flag entered / not entered udp argument
 * @param arp_flag entered / not entered arp argument
 * @param icmp_flag entered / not entered icmp argument
 */
void filter_expression(bool port_flag, int port, bool tcp_flag, bool udp_flag, bool arp_flag, bool icmp_flag) {
    char null[256] = "";
    char temp[50] = "";
    if (icmp_flag) {
        strcat(expression, "icmp");
    }
    if (arp_flag) {
        if (strcmp(expression, null) == 0) {
            strcat(expression, "arp");
        } else {
            strcat(expression, " or arp");
        }
    }
    if (tcp_flag) {
        if (port_flag) {
            if (strcmp(expression, null) == 0) {
                sprintf(temp, "(tcp and port %d)", port);
                strcat(expression, temp);
            } else {
                sprintf(temp, " or (tcp and port %d)", port);
                strcat(expression, temp);
            }
        } else {
            if (strcmp(expression, null) == 0) {
                strcat(expression, "tcp");
            } else {
                strcat(expression, " or tcp");
            }
        }
    }
    if (udp_flag) {
        if (port_flag) {
            if (strcmp(expression, null) == 0) {
                sprintf(temp, "(udp and port %d)", port);
                strcat(expression, temp);
            } else {
                sprintf(temp, " or (udp and port %d)", port);
                strcat(expression, temp);
            }
        } else {
            if (strcmp(expression, null) == 0) {
                strcat(expression, "udp");
            } else {
                strcat(expression, " or udp");
            }
        }
    }
}

/**
 * @brief function to print all available devices
 * @param errbuf error buffer
 */
void findalldevs(char *errbuf) {
    pcap_if_t *list, *device;
    if (pcap_findalldevs(&list, errbuf)) {
        printf("Error finding devices : %s", errbuf);
        exit(EXIT_FAILURE);
    }

    for (device = list; device != NULL; device = device->next) {
        printf("%s \n", device->name);
    }
    pcap_freealldevs(list);
}

/**
 * @brief function to print data of packet
 * @param data data
 * @param len data length
 */
void print_data(const u_char *data, unsigned int len) {
    // code inspired: https://www.binarytides.com/packet-sniffer-code-c-linux/
    int i, j;
    int line = 1;
    printf("\n");
    printf("0x0000:");
    for (i = 0; i < len; i++) {
        if (i != 0 && i % 16 == 0) {
            printf("         ");
            for (j = i - 16; j < i; j++) {
                if (data[j] >= 32 && data[j] <= 128)
                    printf("%c", (unsigned char) data[j]);

                else printf(".");
            }
            printf("\n");

            if (line < 10) printf("0x00%d:", line++ * 10);
            else if (line < 100) printf("0x0%d:", line++ * 10);
            else printf("0x%d:", line++ * 10);
        }

        if (i % 16 == 0) printf("   ");
        printf(" %02X", (unsigned int) data[i]);

        if (i == len - 1) {
            for (j = 0; j < 15 - i % 16; j++) {
                printf("   ");
            }

            printf("         ");

            for (j = i - i % 16; j <= i; j++) {
                if (data[j] >= 32 && data[j] <= 128) {
                    printf("%c", (unsigned char) data[j]);
                } else {
                    printf(".");
                }
            }

            printf("\n");
        }
    }
}

/**
 * @brief function to print ipv4 address
 * @param buffer buffer
 */
void print_ip(const u_char *buffer) {
    // code inspired: https://www.binarytides.com/packet-sniffer-code-c-linux/
    struct sockaddr_in source, dest;
    struct iphdr *iph = (struct iphdr *) (buffer + sizeof(struct ethhdr));
    memset(&source, 0, sizeof(source));
    source.sin_addr.s_addr = iph->saddr;

    memset(&dest, 0, sizeof(dest));
    dest.sin_addr.s_addr = iph->daddr;

    printf("src IP: %s\n", inet_ntoa(source.sin_addr));
    printf("dst IP: %s\n", inet_ntoa(dest.sin_addr));
}

/**
 * @brief function to print ipv6 address
 * @param buffer buffer
 */
void print_ipv6(const u_char *buffer) {
    struct sockaddr_in6 source, dest;
    struct ip6_hdr *iph = (struct ip6_hdr *) (buffer + sizeof(struct ethhdr));

    source.sin6_addr = iph->ip6_src;
    dest.sin6_addr = iph->ip6_dst;

    char sourcestring[INET6_ADDRSTRLEN];
    char deststring[INET6_ADDRSTRLEN];
    // https://man7.org/linux/man-pages/man3/inet_ntop.3.html
    inet_ntop(AF_INET6, &source.sin6_addr, sourcestring, INET6_ADDRSTRLEN);
    inet_ntop(AF_INET6, &dest.sin6_addr, deststring, INET6_ADDRSTRLEN);

    printf("src IP: %s\n", sourcestring);
    printf("dst IP: %s\n", deststring);
}

/**
 * @brief function to print info about tcp protocol
 * @param buffer buffer
 * @param len data length
 */
void print_tcp(const u_char *buffer, unsigned int len) {
    // code inspired: https://www.binarytides.com/packet-sniffer-code-c-linux/
    unsigned short iphdrlen;
    struct iphdr *iph = (struct iphdr *) (buffer + sizeof(struct ethhdr));
    iphdrlen = iph->ihl * 4;

    struct tcphdr *tcph = (struct tcphdr *) (buffer + iphdrlen + sizeof(struct ethhdr));

    printf("src port: %u\n", ntohs(tcph->source));
    printf("dst port: %u\n", ntohs(tcph->dest));

    print_data(buffer, len);
}

/**
 * @brief function to print info about udp protocol
 * @param buffer buffer
 * @param len data length
 */
void print_udp(const u_char *buffer, unsigned int len) {
    // code inspired: https://www.binarytides.com/packet-sniffer-code-c-linux/
    unsigned short iphdrlen;

    struct iphdr *iph = (struct iphdr *) (buffer + sizeof(struct ethhdr));
    iphdrlen = iph->ihl * 4;

    struct udphdr *udph = (struct udphdr *) (buffer + iphdrlen + sizeof(struct ethhdr));

    printf("src port: %u\n", ntohs(udph->source));
    printf("dst port: %u\n", ntohs(udph->dest));

    print_data(buffer, len);
}

/**
 * @brief function to proccess packet
 * @param args description
 * @param header session handle
 * @param buffer buffer
 */
void proccess_packet(u_char *args, const struct pcap_pkthdr *header, const u_char *buffer) {
    // code inspired: https://www.binarytides.com/packet-sniffer-code-c-linux/
    struct ethhdr *eth = (struct ethhdr *) buffer;
    unsigned int len = header->len;

    // gets timestamp string
    char time[30];
    strftime(time, sizeof(time), "%Y-%m-%dT-%H:%M:%S", localtime(&header->ts.tv_sec));

    // prints timestamp
    // https://man7.org/linux/man-pages/man3/strftime.3.html
    printf("timestamp: %s.%06ldZ\n", time, header->ts.tv_usec);

    // prints source MAC, dst MAC and frame lenght
    printf("src MAC: %.2X:%.2X:%.2X:%.2X:%.2X:%.2X \n", eth->h_source[0], eth->h_source[1], eth->h_source[2],
           eth->h_source[3], eth->h_source[4], eth->h_source[5]);
    printf("dst MAC: %.2X:%.2X:%.2X:%.2X:%.2X:%.2X \n", eth->h_dest[0], eth->h_dest[1], eth->h_dest[2], eth->h_dest[3],
           eth->h_dest[4], eth->h_dest[5]);
    printf("frame lenght: %d bytes \n", header->caplen);

    struct ether_header ether = *(struct ether_header *) buffer;

    if (ntohs(ether.ether_type) == ETHERTYPE_IP) {
        // only ipv4 protocols
        struct iphdr *iph = (struct iphdr *) (buffer + sizeof(struct ethhdr));
        print_ip(buffer);
        switch (iph->protocol) {
            // https://en.wikipedia.org/wiki/List_of_IP_protocol_numbers
            // ICMP protocol
            case 1:
                print_data(buffer, len);
                break;
                // TCP protocol
            case 6:
                print_tcp(buffer, len);
                break;
                // UDP protocol
            case 17:
                print_udp(buffer, len);
                break;
            default:
                break;
        }
    } else if (ntohs(ether.ether_type) == ETHERTYPE_IPV6) {
        // only ipv6 protocols
        print_ipv6(buffer);
        struct ip6_hdr *iph = (struct ip6_hdr *) (buffer + sizeof(struct ethhdr));
        uint8_t protocol = iph->ip6_ctlun.ip6_un1.ip6_un1_nxt;
        switch (protocol) {
            // https://en.wikipedia.org/wiki/List_of_IP_protocol_numbers
            // ICMPv6 protocol
            case 58:
                print_data(buffer, len);
                break;
                // TCP protocol
            case 6:
                print_tcp(buffer, len);
                break;
                // UDP protocol
            case 17:
                print_udp(buffer, len);
                break;
            default:
                break;
        }
    } else if (ntohs(ether.ether_type) == ETHERTYPE_ARP) {
        // ARP protocol
        print_data(buffer, len);
    }
}

int main(int argc, char **argv) {
    int c;
    char interface[256];
    int port;
    int number_packets = 1;
    char errbuf[PCAP_ERRBUF_SIZE];

    bool inter_flag = false;
    bool port_flag = false;
    bool tcp_flag = false;
    bool udp_flag = false;
    bool arp_flag = false;
    bool icmp_flag = false;

    struct bpf_program fp;
    bpf_u_int32 mask;
    bpf_u_int32 net;

    signal(SIGINT, my_handler);

    // parse arguments with library getopt.h
    // code inspired: https://www.informit.com/articles/article.aspx?p=175771&seqNum=3
    while (1) {
        static struct option long_options[] = {
                {"interface", optional_argument, 0, 'i'},
                {"tcp",       no_argument,       0, 't'},
                {"udp",       no_argument,       0, 'u'},
                {"arp",       no_argument,       0, 'r'},
                {"icmp",      no_argument,       0, 'm'},
                {"help",      no_argument,       0, 'h'},
                {0, 0,                           0, 0}
        };

        c = getopt_long(argc, argv, "hi::p:n:tu",
                        long_options, NULL);
        if (c == -1)
            break;

        switch (c) {
            case 'h':
                printf("help: <usage> [-i rozhraní | --interface rozhraní] {-p \u00AD\u00ADport} {[--tcp|-t] [--udp|-u] [--arp] [--icmp] } {-n num}\n");
                exit(EXIT_SUCCESS);
            case 'i':
                inter_flag = true;
                if (OPTIONAL_ARGUMENT_IS_PRESENT) {
                    strcpy(interface, optarg);
                } else {
                    findalldevs(errbuf);
                    exit(EXIT_SUCCESS);
                }
                break;
            case 'p':
                port = atoi(optarg);
                port_flag = true;
                if(port < 1) {
                    fprintf(stderr, "port number must be greater than one \n");
                    exit(EXIT_FAILURE);
                }
                break;
            case 'n':
                number_packets = atoi(optarg);
                if(number_packets < 1){
                    fprintf(stderr, "number of packets must be greater than one \n");
                    exit(EXIT_FAILURE);
                }
                break;
            case 't':
                tcp_flag = true;
                break;
            case 'u':
                udp_flag = true;
                break;
            case 'r':
                arp_flag = true;
                break;
            case 'm':
                icmp_flag = true;
                break;
            case '?':
                printf("unrecognized argument \n");
                break;
            default:
                printf("?? getopt returned character code 0%o ??\n", c);
        }
    }
    // if missing -i parameter, shows all available devices
    if (inter_flag == false) {
        findalldevs(errbuf);
        exit(EXIT_SUCCESS);
    }
    if (!tcp_flag && !udp_flag && !arp_flag && !icmp_flag) {
        tcp_flag = true;
        udp_flag = true;
        arp_flag = true;
        icmp_flag = true;
    }

    // opening packet for sniffing
    // code inspired: https://www.tcpdump.org/pcap.html
    handle = pcap_open_live(interface, BUFSIZ, 1, 1, errbuf);

    if (handle == 0) {
        fprintf(stderr, "Couldn't open interface %s : %s \n", interface, errbuf);
        exit(EXIT_FAILURE);
    }

    // control of support Ether header type
    if (pcap_datalink(handle) != DLT_EN10MB) {
        fprintf(stderr, "Device %s doesn't provide Ethernet headers - not supported\n", errbuf);
        exit(EXIT_FAILURE);
    }

    // sets netmask of device
    if (pcap_lookupnet(interface, &net, &mask, errbuf) == -1) {
        fprintf(stderr, "Couldn't get netmask for device %s: %s\n", interface, errbuf);
        net = 0;
        mask = 0;
    }

    // sets global string expression to filter
    filter_expression(port_flag, port, tcp_flag, udp_flag, arp_flag, icmp_flag);

    // compiles filter
    if (pcap_compile(handle, &fp, expression, 0, net) == -1) {
        fprintf(stderr, "Couldn't compile filter %s \n", errbuf);
        exit(EXIT_FAILURE);
    }

    // sets filter
    if (pcap_setfilter(handle, &fp) == -1) {
        fprintf(stderr, "Couldn't set filter %s \n", errbuf);
        exit(EXIT_FAILURE);
    }

    // loop proccess_packet until equals number_packets
    pcap_loop(handle, number_packets, proccess_packet, NULL);

    pcap_close(handle);
    return 0;
}
