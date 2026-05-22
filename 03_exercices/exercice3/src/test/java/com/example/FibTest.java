package com.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class FibTest {
    private Fib fib;

    @Test
    public void shouldReturnNonEmptyListWhenRangeIsOne(){
        //Arrange
        int range = 1;
        fib = new Fib(range);

        //Act
        List<Integer> result = fib.getFibSeries();

        //Assert
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    public void shouldContainZeroWhenRangeIsOne(){
        //Arrange
        int range = 1;
        fib = new Fib(range);

        //Act
        List<Integer> result = fib.getFibSeries();

        //Assert
        Assertions.assertTrue(result.contains(0));
    }

    @Test
    public void shouldContainThreeWhenRangeIsSix(){
        //Arrange
        int range = 6;
        fib = new Fib(range);

        //Act
        List<Integer> result = fib.getFibSeries();

        //Assert
        Assertions.assertTrue(result.contains(3));
    }

    @Test
    public void shouldReturnSixElementsWhenRangeIsSix(){
        //Arrange
        int range = 6;
        int resultSize = 6;
        fib = new Fib(range);

        //Act
        List<Integer> result = fib.getFibSeries();

        //Assert
        Assertions.assertEquals(resultSize,result.size());
    }

    @Test
    public void shouldNotContainFourWhenRangeIsSix(){
        //Arrange
        int range = 6;
        fib = new Fib(range);

        //Act
        List<Integer> result = fib.getFibSeries();

        //Assert
        Assertions.assertFalse(result.contains(4));
    }

    @Test
    public void shouldReturnExpectedFibonacciSeriesWhenRangeIsSix(){
        //Arrange
        List<Integer> resultAwait = List.of(0,1,1,2,3,5);
        int range = 6;
        fib = new Fib(range);

        //Act
        List<Integer> result = fib.getFibSeries();

        //Assert
        Assertions.assertEquals(resultAwait,result);

    }

    @Test
    public void shouldReturnSortedAscendingSeriesWhenRangeIsSix(){
        //Arrange
        int range = 6;
        fib = new Fib(range);

        //Act
        List<Integer> result = fib.getFibSeries();
        List<Integer> expected = new ArrayList<>(result);
        expected.sort(null);
//        Collections.sort(expected);

        //Assert
        Assertions.assertEquals(expected,result);
    }

}
