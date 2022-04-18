//======== Copyright (c) 2021, FIT VUT Brno, All rights reserved. ============//
//
// Purpose:     White Box - Tests suite
//
// $NoKeywords: $ivs_project_1 $white_box_code.cpp
// $Author:     Vojtěch Hájek <xhajek51@stud.fit.vutbr.cz>
// $Date:       $2021-01-04
//============================================================================//
/**
 * @file white_box_tests.cpp
 * @author Vojtěch Hájek
 * 
 * @brief Implementace testu prace s maticemi.
 */

#include "gtest/gtest.h"
#include "white_box_code.h"

//============================================================================//
// ** ZDE DOPLNTE TESTY **
//
// Zde doplnte testy operaci nad maticemi. Cilem testovani je:
// 1. Dosahnout maximalniho pokryti kodu (white_box_code.cpp) testy.
// 2. Overit spravne chovani operaci nad maticemi v zavislosti na rozmerech 
//    matic.
//============================================================================//

class MatrixSquare : public ::testing::Test{
protected:
    Matrix x11;
    Matrix x22{2,2};
    Matrix x33{3,3};
    Matrix x44{4,4};
    virtual void SetUp() {
        x11.set(0,0,1);

        std::vector<std::vector< double > > values = {
                {20, 15},
                {10, 8}
        };
        x22.set(values);

        values = {
                {7, 2, 1},
                {0, 3, -1},
                {-3, 4, -2}
        };
        x33.set(values);
        values = {
                {7, 2, 1, 1},
                {0, 3, -1, 1},
                {-3, 4, -2, 1},
                {-3, 4, -2, 1}
        };
        x44.set(values);
    }
};

class MatrixNotSquare : public ::testing::Test{

protected:
    Matrix x12{1,2};
    virtual void SetUp() {

        std::vector<std::vector< double > >values={
                {1, 2}
        };
        x12.set(values);
    }
};


TEST(Basic, constructor){
    Matrix x{};
    EXPECT_EQ(x.get(0, 0), 0);
    Matrix x2{2, 2};
    EXPECT_EQ(x2.get(1,1), 0);
    EXPECT_ANY_THROW(Matrix(-1, 2));
    EXPECT_ANY_THROW(Matrix(0,1));
    EXPECT_ANY_THROW(Matrix(1,0));
}

TEST(Basic, setOne){
    Matrix x{1,1};
    x.set(0,0,2);
    EXPECT_EQ(x.get(0,0), 2);
    EXPECT_FALSE(x.set(2, 2, 2));
}

TEST(Basic, setField){
    Matrix x{2,2};
    std::vector<std::vector< double > > values = {
            {20, 15},
            {10, 8}
    };
    EXPECT_TRUE(x.set(values));
    Matrix x2{1,2};
    values = {
            {20, 15},
            {10, 8}
    };
    EXPECT_FALSE(x2.set(values));
    Matrix x3{2,2};
    values = {
            {20, 15},
            {10}
    };
    EXPECT_FALSE(x3.set(values));
}


TEST(Basic, get){
    Matrix x{};
    EXPECT_EQ(x.get(0, 0), 0);
    EXPECT_ANY_THROW(x.get(1,0));
}

TEST_F(MatrixSquare, equals){
    Matrix x2(2,2);
    std::vector<std::vector< double > > values = {
            {20, 15},
            {10, 8}
    };
    x2.set(values);
    EXPECT_TRUE(x22.operator==(x2));

    Matrix x3(2,2);
    std::vector<std::vector< double > > values2 = {
            {0, 0},
            {0, 0}
    };
    x3.set(values2);
    EXPECT_FALSE(x22.operator==(x3));
    std::vector<std::vector< double > > values3 = {
            {0, 0}
    };
    Matrix x4(1,2);
    x4.set(values3);
    EXPECT_ANY_THROW(x22.operator==(x4));
}

TEST_F(MatrixSquare, sum){
    Matrix x2(2,2);
    std::vector<std::vector< double > > values = {
            {1, 1},
            {1, 1}
    };
    x2.set(values);

    x22=x22.operator+(x2);
    Matrix result(2,2);
    values = {
            {21, 16},
            {11, 9}
    };
    result.set(values);
    EXPECT_EQ(result, x22);
    Matrix x3(1,2);
    values = {
            {1, 1}
    };
    x2.set(values);
    EXPECT_ANY_THROW(x22.operator+(x3));
}

TEST_F(MatrixSquare, multiplyByMatrix){
    Matrix xTemp(2,2);
    std::vector<std::vector< double > > values = {
            {2, 2},
            {2, 2}
    };
    xTemp.set(values);

    x22=x22.operator*(xTemp);
    Matrix result(2,2);
    values = {
            {70, 70},
            {36, 36}
    };
    result.set(values);
    EXPECT_EQ(result, x22);

    Matrix x3(1,2);
    values = {
            {2, 2}
    };
    x3.set(values);
    EXPECT_ANY_THROW(x22.operator*(x3));
}

TEST_F(MatrixSquare, multiplyByValue){
    double y=2;
    x22=x22.operator*(y);

    Matrix result(2,2);
    std::vector<std::vector< double > > values = {
            {40, 30},
            {20, 16}
    };
    result.set(values);
    EXPECT_TRUE(x22.operator==(result));
}

TEST_F(MatrixSquare, solveEquation){
    std::vector<double, std::allocator<double>> values = {
            {2, 2}
    };
    std::vector<double> res=x22.solveEquation(values);
    std::vector<double> expectedResult={{-1.4, 2}};
    EXPECT_EQ(res, expectedResult);


    std::vector<std::vector< double > > values2 = {
            {1, -2},
            {-2, 4}
    };
    x22.set(values2);
    EXPECT_ANY_THROW(x22.solveEquation(values));

    values = {2};
    res=x11.solveEquation(values);
    expectedResult={{2}};
    EXPECT_EQ(res, expectedResult);

    values = {
            {2, 2, 2}
    };
    res=x33.solveEquation(values);
    expectedResult={{2, -2, -8}};
    EXPECT_EQ(res, expectedResult);

    values = {
            {2, 2, 2, 2}
    };
    EXPECT_ANY_THROW(x44.solveEquation(values));
}

TEST_F(MatrixSquare, transpose){
    x22=x22.transpose();
    Matrix result(2,2);
    std::vector<std::vector< double > > values = {
            {20, 10},
            {15, 8}
    };
    result.set(values);
    EXPECT_EQ(result, x22);

    x11=x11.transpose();
    EXPECT_EQ(x11.get(0,0), 1);
}

TEST_F(MatrixSquare, inverse){
    x22=x22.inverse();
    Matrix result(2,2);
    std::vector<std::vector< double > > values = {
            {0.8, -1.5},
            {-1, 2}
    };
    result.set(values);
    EXPECT_EQ(result, x22);

    Matrix xTemp(2,2);
    values ={
            {1, -2},
            {-2, 4}
    };
    xTemp.set(values);
    EXPECT_ANY_THROW(xTemp.inverse());

    EXPECT_ANY_THROW(x11.inverse());

    x33=x33.inverse();
    Matrix result33(3,3);
    values = {
            {-2, 8, -5},
            {3, -11, 7},
            {9, -34, 21}
    };
    result33.set(values);
    EXPECT_EQ(result33, x33);
}

TEST_F(MatrixNotSquare, equals){
    Matrix xTemp(1,2);
    std::vector<std::vector< double > >values={
            {1, 2}
    };
    xTemp.set(values);
    EXPECT_TRUE(x12.operator==(xTemp));
}

TEST_F(MatrixNotSquare, sum){
    Matrix xTemp(1,2);
    std::vector<std::vector< double > > values={
            {0,0}
    };
    xTemp.set(values);
    x12=x12.operator+(xTemp);
    Matrix result(1,2);
    values={
            {1, 2}
    };
    result.set(values);

    EXPECT_EQ(x12, result);
}

TEST_F(MatrixNotSquare, multiplyByMatrix)
{
   Matrix xTemp (2,1);
    std::vector<std::vector< double > > values={
            {2},
            {2}
    };
    xTemp.set(values);
    x12=x12.operator*(xTemp);
    Matrix result(1,1);
    result.set(0,0,6);
    EXPECT_EQ(result, x12);
}

TEST_F(MatrixNotSquare, multiplyByValue){
    double y=2;
    x12=x12.operator*(y);
    Matrix result (1,2);
    std::vector<std::vector< double > > values={
            {2, 4},
    };
    result.set(values);
    EXPECT_EQ(result, x12);
}

TEST_F(MatrixNotSquare, solveEquation){
    std::vector<double, std::allocator<double>> values = {
            {2, 2}
    };
    EXPECT_ANY_THROW(x12.solveEquation(values));
    Matrix notSquare2(1,3);
    std::vector<std::vector< double > >values2={
            {1, 2, 3}
    };
    notSquare2.set(values2);
    EXPECT_ANY_THROW(notSquare2.solveEquation(values));
}

TEST_F(MatrixNotSquare, transpose){
    EXPECT_NO_THROW(x12.transpose());
}

TEST_F(MatrixNotSquare, inverse){
    EXPECT_ANY_THROW(x12.inverse());
}

/*** Konec souboru white_box_tests.cpp ***/
