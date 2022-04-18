//======== Copyright (c) 2017, FIT VUT Brno, All rights reserved. ============//
//
// Purpose:     Red-Black Tree - public interface tests
//
// $NoKeywords: $ivs_project_1 $black_box_tests.cpp
// $Author:     Vojtěch Hájek <xhajek51@stud.fit.vutbr.cz>
// $Date:       $2017-01-04
//============================================================================//
/**
 * @file black_box_tests.cpp
 * @author Vojtěch Hájek
 * 
 * @brief Implementace testu binarniho stromu.
 */

#include <vector>

#include "gtest/gtest.h"

#include "red_black_tree.h"

//============================================================================//
// ** ZDE DOPLNTE TESTY **
//
// Zde doplnte testy Red-Black Tree, testujte nasledujici:
// 1. Verejne rozhrani stromu
//    - InsertNode/DeleteNode a FindNode
//    - Chovani techto metod testuje pro prazdny i neprazdny strom.
// 2. Axiomy (tedy vzdy platne vlastnosti) Red-Black Tree:
//    - Vsechny listove uzly stromu jsou *VZDY* cerne.
//    - Kazdy cerveny uzel muze mit *POUZE* cerne potomky.
//    - Vsechny cesty od kazdeho listoveho uzlu ke koreni stromu obsahuji
//      *STEJNY* pocet cernych uzlu.
//============================================================================//
class EmptyTree : public :: testing::Test
{
protected:
    BinaryTree tree;
};
class NonEmptyTree : public :: testing::Test
{

protected:
    virtual void SetUp() {
        int values[] = { 5, 10, 15, 20, 30, 40, 50, 55, 60, 65, 70, 80, 85, 90 };
        for (auto value : values) {
            tree.InsertNode(value);
        }
    }

    BinaryTree tree;
};
class TreeAxioms: public :: testing::Test
{

protected:
    virtual void SetUp() {
        int values[] = { 5, 10, 15, 20, 30, 40, 50, 55, 60, 65, 70, 80, 85, 90 };
        for (auto value : values) {
            tree.InsertNode(value);
        }
    }

    BinaryTree tree;
};

TEST_F(EmptyTree, InsertNode){
    EXPECT_TRUE(tree.GetRoot()==NULL);
    auto resTree= tree.InsertNode(8);
    EXPECT_TRUE(resTree.first);
    EXPECT_EQ(resTree.second->key, 8);

    auto resTree2=tree.InsertNode(8);
    EXPECT_FALSE(resTree2.first);
    EXPECT_EQ(resTree2.second, resTree.second);

}

TEST_F(EmptyTree, DeleteNode){
    EXPECT_TRUE(tree.GetRoot()==NULL);
    EXPECT_FALSE(tree.DeleteNode(0));
}

TEST_F(EmptyTree, FindNode){
    EXPECT_TRUE(tree.GetRoot()==NULL);
    EXPECT_TRUE(tree.FindNode(0)==NULL);
}

TEST_F(NonEmptyTree, InsertNode){
    auto resTree=tree.InsertNode(5);
    EXPECT_FALSE(resTree.first);

    resTree=tree.InsertNode(95);
    EXPECT_TRUE(resTree.first);
    EXPECT_EQ(resTree.second->key, 95);

}

TEST_F(NonEmptyTree, DeleteNode){
    int values[] = { 5, 10, 15, 20, 30, 40, 50, 55, 60, 65, 70, 80, 85, 90 };
    for(int i = 0; i < 14; ++i) {
        EXPECT_TRUE(tree.DeleteNode(values[i]));
    }
    EXPECT_TRUE(tree.GetRoot()==NULL);
}

TEST_F(NonEmptyTree, FindNode){
    int values[] = { 5, 10, 15, 20, 30, 40, 50, 55, 60, 65, 70, 80, 85, 90 };
    for(int i = 0; i < 14; ++i) {
        EXPECT_TRUE(tree.FindNode(values[i]));
    }
    EXPECT_FALSE(tree.FindNode(0));
}

TEST_F(TreeAxioms, Axiom1){
    std::vector<Node_t*> outLeafNodes {};
    tree.GetLeafNodes(outLeafNodes);
    for (auto value : outLeafNodes) {
        EXPECT_EQ(value->color, 1);
    }
}

TEST_F(TreeAxioms, Axiom2){
    std::vector<Node_t*> outNonLeafNodes {};
    tree.GetNonLeafNodes(outNonLeafNodes);
    for (auto value : outNonLeafNodes) {
        //EXPECT_EQ(value->color, 0);
        if(value->color, 0){
            EXPECT_EQ(value->pLeft->color, 1);
            EXPECT_EQ(value->pRight->color, 1);
        }
    }
}

TEST_F(TreeAxioms, Axiom3){
    std::vector<Node_t*> outLeafNodes {};
    std::vector<int> countJumps{};
    tree.GetLeafNodes(outLeafNodes);
    int jumps;
    for(auto value : outLeafNodes){
        jumps=0;
        while (value != tree.GetRoot()){
            if(value->color==1){
                jumps++;
            }
            value=value->pParent;
        }
        countJumps.push_back(jumps);
    }
    int firstJump=countJumps.front();
    for(auto jump : countJumps){
        EXPECT_EQ(jump, firstJump);
    }
}



/*** Konec souboru black_box_tests.cpp ***/
