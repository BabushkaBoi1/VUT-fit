//======== Copyright (c) 2021, FIT VUT Brno, All rights reserved. ============//
//
// Purpose:     Test Driven Development - priority queue code
//
// $NoKeywords: $ivs_project_1 $tdd_code.cpp
// $Author:     Vojtěch Hájek <xhajek51@stud.fit.vutbr.cz>
// $Date:       $2021-01-04
//============================================================================//
/**
 * @file tdd_code.cpp
 * @author Vojtěch Hájek
 * 
 * @brief Implementace metod tridy prioritni fronty.
 */

#include <stdlib.h>
#include <stdio.h>

#include "tdd_code.h"

//============================================================================//
// ** ZDE DOPLNTE IMPLEMENTACI **
//
// Zde doplnte implementaci verejneho rozhrani prioritni fronty (Priority Queue)
// 1. Verejne rozhrani fronty specifikovane v: tdd_code.h (sekce "public:")
//    - Konstruktor (PriorityQueue()), Destruktor (~PriorityQueue())
//    - Metody Insert/Remove/Find a GetHead
//    - Pripadne vase metody definovane v tdd_code.h (sekce "protected:")
//
// Cilem je dosahnout plne funkcni implementace prioritni fronty implementovane
// pomoci tzv. "double-linked list", ktera bude splnovat dodane testy 
// (tdd_tests.cpp).
//============================================================================//

PriorityQueue::PriorityQueue()
{

}

PriorityQueue::~PriorityQueue()
{
    if(m_pHead!= nullptr){
        auto list=m_pHead;
        m_pHead= nullptr;
        while (list->pNext!= nullptr){
            list=list->pNext;
            auto tempList=list;
            tempList->pNext= nullptr;
        }
    }
}

void PriorityQueue::Insert(int value)
{
        if(m_pHead== nullptr){
            m_pHead = new Element_t{
                .pNext = nullptr,
                .value=value
            };
            return;
        }
        auto list=m_pHead;
        if(value > m_pHead->value){
            m_pHead=new Element_t{
                .pNext=list,
                .value=value
            };
            return;
        }

        while (list->pNext!= nullptr && list->pNext->value > value){
            list=list->pNext;
        }
        auto tempList=list->pNext;
        list->pNext=new Element_t{
            .pNext=tempList,
            .value=value
        };
        return;

}

bool PriorityQueue::Remove(int value)
{
    if(m_pHead== nullptr){
        return false;
    }
    auto list=m_pHead;
    auto tempList=list->pNext;

    if(m_pHead->value==value){
        m_pHead= nullptr;
        m_pHead=tempList;
        return true;
    }

    while (list->pNext!= nullptr && list->pNext->value != value){
        list=list->pNext;
        tempList=tempList->pNext;
    }
    if (list->pNext!= nullptr){
        list->pNext= nullptr;
        list->pNext=tempList->pNext;
        return true;
    }
    return false;

}

PriorityQueue::Element_t *PriorityQueue::Find(int value)
{
    auto list=m_pHead;
    while (list!= nullptr && list->value != value){
        list=list->pNext;
    }
    return list;
}

size_t PriorityQueue::Length()
{
    auto size=0;
    if(m_pHead!= nullptr){
        auto list=m_pHead;
        while (list != nullptr){
            list=list->pNext;
            size++;
        }
    }
	return size;
}

PriorityQueue::Element_t *PriorityQueue::GetHead()
{
    return m_pHead;
}

/*** Konec souboru tdd_code.cpp ***/
