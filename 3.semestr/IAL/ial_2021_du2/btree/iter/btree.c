/*
 * Binárny vyhľadávací strom — iteratívna varianta
 *
 * S využitím dátových typov zo súboru btree.h, zásobníkov zo súborov stack.h a
 * stack.c a pripravených kostier funkcií implementujte binárny vyhľadávací
 * strom bez použitia rekurzie.
 */

#include "../btree.h"
#include "stack.h"
#include <stdio.h>
#include <stdlib.h>

/*
 * Inicializácia stromu.
 *
 * Užívateľ musí zaistiť, že incializácia sa nebude opakovane volať nad
 * inicializovaným stromom. V opačnom prípade môže dôjsť k úniku pamäte (memory
 * leak). Keďže neinicializovaný ukazovateľ má nedefinovanú hodnotu, nie je
 * možné toto detegovať vo funkcii.
 */
void bst_init(bst_node_t **tree) {
  (*tree) = NULL;
}

/*
 * Nájdenie uzlu v strome.
 *
 * V prípade úspechu vráti funkcia hodnotu true a do premennej value zapíše
 * hodnotu daného uzlu. V opačnom prípade funckia vráti hodnotu false a premenná
 * value ostáva nezmenená.
 *
 * Funkciu implementujte iteratívne bez použitia vlastných pomocných funkcií.
 */
bool bst_search(bst_node_t *tree, char key, int *value) {
  while (tree != NULL)
  {
    if(tree->key == key){
      *value = tree->value;
      return true;
    }
    else if(key < tree->key){
        tree=tree->left;
    }
    else{
        tree=tree->right;
    }
  }
  return false;
}

/*
 * Vloženie uzlu do stromu.
 *
 * Pokiaľ uzol so zadaným kľúčom v strome už existuje, nahraďte jeho hodnotu.
 * Inak vložte nový listový uzol.
 *
 * Výsledný strom musí spĺňať podmienku vyhľadávacieho stromu — ľavý podstrom
 * uzlu obsahuje iba menšie kľúče, pravý väčšie.
 *
 * Funkciu implementujte iteratívne bez použitia vlastných pomocných funkcií.
 */
void bst_insert(bst_node_t **tree, char key, int value) {
  bst_node_t *parent = NULL;
  bst_node_t *next = (*tree);

  while (next != NULL)
  {
    parent = next;
    if(key < next->key){
      next = next->left;
    }
    else if(key > next->key){
      next = next->right;
    }
    else{
      next->value = value;
      return;
    }
  }
  if(next == NULL){
    bst_node_t *tmp = (bst_node_t*) malloc(sizeof(bst_node_t));
    tmp->key = key;
    tmp->value = value;
    tmp->left = NULL;   
    tmp->right = NULL;

    if(parent != NULL){
      if(key < parent->key){
        parent->left = tmp;
      }
      else{
        parent->right = tmp;
      }
    }
    else{
      (*tree) = tmp;
    }
  }
}

/*
 * Pomocná funkcia ktorá nahradí uzol najpravejším potomkom.
 *
 * Kľúč a hodnota uzlu target budú nahradené kľúčom a hodnotou najpravejšieho
 * uzlu podstromu tree. Najpravejší potomok bude odstránený. Funkcia korektne
 * uvoľní všetky alokované zdroje odstráneného uzlu.
 *
 * Funkcia predpokladá že hodnota tree nie je NULL.
 *
 * Táto pomocná funkcia bude využitá pri implementácii funkcie bst_delete.
 *
 * Funkciu implementujte iteratívne bez použitia vlastných pomocných funkcií.
 */
void bst_replace_by_rightmost(bst_node_t *target, bst_node_t **tree) {
  if((*tree) != NULL){
    bst_node_t *next = (*tree);
    bst_node_t *parent = NULL;
    while (next->right != NULL)
    {
      parent = next;
      next = next->right;
    }
    if(next->right == NULL){
        bst_node_t *tmp = next;
        target->key = tmp->key;
        target->value = tmp->value;
        parent->right = next->left;
        free(tmp);
    }
  }
}

/*
 * Odstránenie uzlu v strome.
 *
 * Pokiaľ uzol so zadaným kľúčom neexistuje, funkcia nič nerobí.
 * Pokiaľ má odstránený uzol jeden podstrom, zdedí ho otec odstráneného uzla.
 * Pokiaľ má odstránený uzol oba podstromy, je nahradený najpravejším uzlom
 * ľavého podstromu. Najpravejší uzol nemusí byť listom!
 * Funkcia korektne uvoľní všetky alokované zdroje odstráneného uzlu.
 *
 * Funkciu implementujte iteratívne pomocou bst_replace_by_rightmost a bez
 * použitia vlastných pomocných funkcií.
 */
void bst_delete(bst_node_t **tree, char key) {
  if((*tree) != NULL){
    bst_node_t *parent = NULL;
    bst_node_t *next = (*tree);
    while (next != NULL && key != next->key)
    {
      parent = next;
      if(key < next->key){
        next = next->left;
      }
      else{
        next = next->right;
      }
    }
    
    if(next == NULL){
      return;
    }

    if(next->left == NULL && next->right == NULL){
      if(parent->left == next){
         parent->left = NULL;
      }
      else{
        parent->right = NULL;
      }
      free(next);
    }
    else if(next->left == NULL){
      bst_node_t *tmp = next;
      if (next == parent->right)
      {
        parent->right = tmp->right;
      }
      free(tmp);
    }
    else if (next->right == NULL){
      bst_node_t *tmp = next;
      if(next == parent->left){
        parent->left = tmp->left;
      }
      free(tmp);
    }
    else{
      bst_replace_by_rightmost(next, &next->left);
    }
  }
}


/*
 * Zrušenie celého stromu.
 *
 * Po zrušení sa celý strom bude nachádzať v rovnakom stave ako po
 * inicializácii. Funkcia korektne uvoľní všetky alokované zdroje rušených
 * uzlov.
 *
 * Funkciu implementujte iteratívne pomocou zásobníku uzlov a bez použitia
 * vlastných pomocných funkcií.
 */
void bst_dispose(bst_node_t **tree) {
  if((*tree) != NULL){
    stack_bst_t stack;
    stack_bst_init(&stack);
    stack_bst_push(&stack, (*tree));
    
    while (!stack_bst_empty(&stack))
    {
      bst_node_t *tmp = stack_bst_pop(&stack);
      if(tmp->left != NULL){
        stack_bst_push(&stack, tmp->left);
      }
      if(tmp->right != NULL){
        stack_bst_push(&stack, tmp->right);
      }
      free(tmp);
    }
    bst_init(&(*tree));
  }
}

/*
 * Pomocná funkcia pre iteratívny preorder.
 *
 * Prechádza po ľavej vetve k najľavejšiemu uzlu podstromu.
 * Nad spracovanými uzlami zavola bst_print_node a uloží ich do zásobníku uzlov.
 *
 * Funkciu implementujte iteratívne pomocou zásobníku uzlov a bez použitia
 * vlastných pomocných funkcií.
 */
void bst_leftmost_preorder(bst_node_t *tree, stack_bst_t *to_visit) {
    while (tree != NULL)
    {
      bst_print_node(tree);
      stack_bst_push(to_visit, tree);
      tree = tree->left;
    }
}

/*
 * Preorder prechod stromom.
 *
 * Pre aktuálne spracovávaný uzol nad ním zavolajte funkciu bst_print_node.
 *
 * Funkciu implementujte iteratívne pomocou funkcie bst_leftmost_preorder a
 * zásobníku uzlov bez použitia vlastných pomocných funkcií.
 */
void bst_preorder(bst_node_t *tree) {
  if(tree != NULL){
    stack_bst_t stack;
    stack_bst_init(&stack);
    bst_leftmost_preorder(tree, &stack);
    while (!stack_bst_empty(&stack))
    {
      bst_node_t *tmp = stack_bst_pop(&stack);
      if(tmp->right != NULL){
        bst_leftmost_preorder(tmp->right, &stack);
      }
    }
  }
}

/*
 * Pomocná funkcia pre iteratívny inorder.
 *
 * Prechádza po ľavej vetve k najľavejšiemu uzlu podstromu a ukladá uzly do
 * zásobníku uzlov.
 *
 * Funkciu implementujte iteratívne pomocou zásobníku uzlov a bez použitia
 * vlastných pomocných funkcií.
 */
void bst_leftmost_inorder(bst_node_t *tree, stack_bst_t *to_visit) {
  while (tree != NULL)
  {
    stack_bst_push(to_visit, tree);
    tree = tree->left;
  }
}

/*
 * Inorder prechod stromom.
 *
 * Pre aktuálne spracovávaný uzol nad ním zavolajte funkciu bst_print_node.
 *
 * Funkciu implementujte iteratívne pomocou funkcie bst_leftmost_inorder a
 * zásobníku uzlov bez použitia vlastných pomocných funkcií.
 */
void bst_inorder(bst_node_t *tree) {
  if(tree != NULL){
    stack_bst_t stack;
    stack_bst_init(&stack);
    bst_leftmost_inorder(tree, &stack);

    while (!stack_bst_empty(&stack))
    {
      bst_node_t *tmp = stack_bst_pop(&stack);
      bst_print_node(tmp);
      if(tmp->right != NULL){
        bst_leftmost_inorder(tmp->right, &stack);
      }
    }
  }
}

/*
 * Pomocná funkcia pre iteratívny postorder.
 *
 * Prechádza po ľavej vetve k najľavejšiemu uzlu podstromu a ukladá uzly do
 * zásobníku uzlov. Do zásobníku bool hodnôt ukladá informáciu že uzol
 * bol navštívený prvý krát.
 *
 * Funkciu implementujte iteratívne pomocou zásobníkov uzlov a bool hodnôt a bez použitia
 * vlastných pomocných funkcií.
 */
void bst_leftmost_postorder(bst_node_t *tree, stack_bst_t *to_visit, stack_bool_t *first_visit) {
  while (tree != NULL)
  {
    stack_bst_push(to_visit, tree);
    stack_bool_push(first_visit, 1);
    tree = tree->left;
  }
}

/*
 * Postorder prechod stromom.
 *
 * Pre aktuálne spracovávaný uzol nad ním zavolajte funkciu bst_print_node.
 *
 * Funkciu implementujte iteratívne pomocou funkcie bst_leftmost_postorder a
 * zásobníkov uzlov a bool hodnôt bez použitia vlastných pomocných funkcií.
 */
void bst_postorder(bst_node_t *tree) {
  if(tree != NULL){
    stack_bst_t stack1;
    stack_bst_init(&stack1);

    stack_bool_t stack2;
    stack_bool_init(&stack2);

    bst_leftmost_postorder(tree, &stack1, &stack2);
    while (!stack_bst_empty(&stack1))
    {
      bst_node_t *tmp = stack_bst_pop(&stack1);
      bool visited = stack_bool_pop(&stack2);
      if(tmp->right != NULL && visited){
        stack_bst_push(&stack1, tmp);
        stack_bool_push(&stack2, 0);
        bst_leftmost_postorder(tmp->right, &stack1, &stack2);
      }
      else{
        bst_print_node(tmp);
      }
    }
  }
}
