; Vernamova sifra na architekture DLX
; Vojtěch Hájek xhajek51

        .data 0x04          ; zacatek data segmentu v pameti
login:  .asciiz "xhajek51"  ; <-- nahradte vasim loginem
cipher: .space 9 ; sem ukladejte sifrovane znaky (za posledni nezapomente dat 0)

        .align 2            ; dale zarovnavej na ctverice (2^2) bajtu
laddr:  .word login         ; 4B adresa vstupniho textu (pro vypis)
caddr:  .word cipher        ; 4B adresa sifrovaneho retezce (pro vypis)

        .text 0x40          ; adresa zacatku programu v pameti
        .global main        ; 

main:
       	loop:
	lb r15, login(r9) 	; načíst znak z login
	addi r22, r22, 1	; klíč začíná až po x
	lb r12, login(r22)	; šifrovací klíč h/a
	subi r22, r22, 1	; porovnávání skoků s hodnotami 0 a 1
	subi r12, r12, 96	; odečtu abych získal hodnotu o kterou budu posouvat
	slei r2, r15, 96 	; porovnám jestli není číslo
	nop
	bnez r2, ending		; jestli je číslo skočim na konec	
	nop
	nop
	bnez r22, key2		; rozlišení který klíč použít
	nop
	nop
	
				; key1
	add r15, r15, r12	; posun o hodnotu klíče
	slei r2, r15, 122	
	bnez r2, bottomz	
	nop
	nop
	subi r15, r15, 26	; pokud překročí poslední znak ascii "z", pošle ho na "a"

	bottomz:
	addi r22, r22, 1	
	j bottom	
	nop
	nop
	
	key2:			; key2
	sub r15, r15, r12 	; posun o hodnotu klíče
	sgei r2, r15, 97	
	bnez r2, topa
	nop
	nop
	addi r15, r15, 26	; pokud se posune před znak "a", pošle ho nakonec od "z"

	topa:
	subi r22, r22, 1
	
	 
	bottom:
	sb cipher(r9), r15	; uloží výsledný znak šifry
	addi r9, r9, 1		
	j loop			; skočí na začátek loop
	nop
	nop
	
	ending:		
	addi r9, r9, 1
	sb cipher(r9), r0	; na poslední znak přidá znak 0

end:    addi r14, r0, caddr ; <-- pro vypis sifry nahradte laddr adresou caddr
        trap 5  ; vypis textoveho retezce (jeho adresa se ocekava v r14)
        trap 0  ; ukonceni simulace
