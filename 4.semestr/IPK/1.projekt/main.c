#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <signal.h>


#define SERVER_PORT 8080
#define MAXLINE 2048
#define SA struct sockadrr

unsigned sleep(unsigned sec);

struct cpustat {
    unsigned long t_user;
    unsigned long t_nice;
    unsigned long t_system;
    unsigned long t_idle;
    unsigned long t_iowait;
    unsigned long t_irq;
    unsigned long t_softirq;
};

// exit function when signal SIGINT (ctrl+c)
void my_handler(sig_atomic_t s){
    exit(1);
}

// function returns cpu stats from /proc/stat
void get_cpu_stats(struct cpustat *st, int cpunum) {
    FILE *fp = fopen("/proc/stat", "r");
    char cpun[255];
    fscanf(fp, "%s %ld %ld %ld %ld %ld %ld %ld", cpun, &(st->t_user), &(st->t_nice),
           &(st->t_system), &(st->t_idle), &(st->t_iowait), &(st->t_irq),
           &(st->t_softirq));
    fclose(fp);
    return;
}

// function returns percentage of cpu usage
double calculate_usage(struct cpustat *prev, struct cpustat *cur) {
    int idle_prev = (prev->t_idle) + (prev->t_iowait);
    int idle_cur = (cur->t_idle) + (cur->t_iowait);

    int nidle_prev = (prev->t_user) + (prev->t_nice) + (prev->t_system) + (prev->t_irq) + (prev->t_softirq);
    int nidle_cur = (cur->t_user) + (cur->t_nice) + (cur->t_system) + (cur->t_irq) + (cur->t_softirq);

    int total_prev = idle_prev + nidle_prev;
    int total_cur = idle_cur + nidle_cur;

    double totald = (double) total_cur - (double) total_prev;
    double idled = (double) idle_cur - (double) idle_prev;

    double cpu_perc = (1000 * (totald - idled) / totald + 1) / 10;

    return cpu_perc;
}

// function returns cpu information from /proc/cpuinfo
void get_cpu_name(char *buff) {
    FILE *fp = popen("grep 'model name'  /proc/cpuinfo | awk -F ': ' '{print $2}'", "r");
    if (fp != NULL) {
        fgets(buff, 255, (FILE *) fp);
        pclose(fp);
    }
    return;
}

// function returns hostname from /proc/sys/kernel/hostname
void get_host_name(char *buff) {
    FILE *fp = fopen("/proc/sys/kernel/hostname", "r");
    if (fp != NULL) {
        fgets(buff, 255, (FILE *) fp);
        fclose(fp);
    }
    return;
}

int main(int argc, const char *argv[]) {
    int sockfd, n;
    struct sockaddr_in servaddr;
    socklen_t servaddr_len = sizeof(servaddr);
    char buf[MAXLINE];
    int on = 1;
    signal (SIGINT,my_handler);

    if (argc != 2) {
        fprintf(stderr, "usage: %s <port> \n", argv[0]);
        exit(EXIT_FAILURE);
    }
    int port = atoi(argv[1]);

    if ((sockfd = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP)) < 0) {
        fprintf(stderr, "error with creating the socket!");
        exit(EXIT_FAILURE);
    }

    setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR | SO_REUSEPORT, (const char *) &on, sizeof(int));
    servaddr.sin_family = AF_INET;
    servaddr.sin_port = htons(port);
    servaddr.sin_addr.s_addr = INADDR_ANY;

    if ((n = bind(sockfd, (struct sockaddr *) &servaddr, servaddr_len)) < 0) {
        perror("ERROR: bind");
        exit(EXIT_FAILURE);
    }

    if ((n = listen(sockfd, 128)) < 0) {
        perror("ERROR: listen");
        exit(EXIT_FAILURE);
    }

    while (1) {
        int clientfd = accept(sockfd, (struct sockaddr *) &servaddr, &servaddr_len);
        if (clientfd == -1) {
            perror("ERROR: connection failed");
            exit(EXIT_FAILURE);
        }
        memset(buf, 0, 2048);
        read(clientfd, buf, 2047);

        char response[] = "HTTP/1.1 200 OK\r\nContent-Type: text/plain;\r\n\r\n";
        if (!strncmp(buf, "GET /hostname", 13)) {
            char hostname[255];
            get_host_name(hostname);
            strcat(response, hostname);
        } else if (!strncmp(buf, "GET /cpu-name", 13)) {
            char cpu_name[255];
            get_cpu_name(cpu_name);
            strcat(response, cpu_name);
        } else if (!strncmp(buf, "GET /load", 9)) {
            struct cpustat st0_0, st0_1;
            get_cpu_stats(&st0_0, -1);
            sleep(1);
            get_cpu_stats(&st0_1, -1);
            double cpu_usage = calculate_usage(&st0_0, &st0_1);
            char output[10];
            sprintf(output, "%d%%", (int)cpu_usage);
            strcat(response, output);
        } else {
            strcat(response, "400 Bad Request\n");
        }
        send(clientfd, response, strlen(response), 0);
        close(clientfd);
    }
    return 0;

}