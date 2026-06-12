package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FrameTest {

    @Mock
    private IGenerateur generateur;

    private Frame frame;

    @Test
    void shouldIncreaseScoreWhenFirstRollIsMadeInStandardFrame() {
        // Arrange
        frame = new Frame(generateur, false);
        when(generateur.randomPin(10)).thenReturn(4);

        // Act
        boolean rollAccepted = frame.makeRoll();

        // Assert
        assertTrue(rollAccepted);
        assertEquals(4, frame.getScore());
    }

    @Test
    void shouldIncreaseScoreWhenSecondRollIsMadeInStandardFrame() {
        // Arrange
        frame = new Frame(generateur, false);
        when(generateur.randomPin(10)).thenReturn(3);
        when(generateur.randomPin(7)).thenReturn(4);

        // Act
        frame.makeRoll();
        frame.makeRoll();

        // Assert
        assertEquals(7, frame.getScore());
    }

    @Test
    void shouldRejectSecondRollWhenStandardFrameStartsWithStrike() {
        // Arrange
        frame = new Frame(generateur, false);
        when(generateur.randomPin(10)).thenReturn(10);

        // Act
        assertTrue(frame.makeRoll());
        boolean secondRollAccepted = frame.makeRoll();

        // Assert
        assertFalse(secondRollAccepted);
        assertEquals(10, frame.getScore());
    }

    @Test
    void shouldRejectThirdRollWhenStandardFrameAlreadyHasTwoRolls() {
        // Arrange
        frame = new Frame(generateur, false);
        when(generateur.randomPin(10)).thenReturn(3);
        when(generateur.randomPin(7)).thenReturn(4);

        // Act
        frame.makeRoll();
        frame.makeRoll();
        boolean thirdRollAccepted = frame.makeRoll();

        // Assert
        assertFalse(thirdRollAccepted);
        assertEquals(7, frame.getScore());
    }

    @Test
    void shouldIncreaseScoreWhenSecondRollIsMadeAfterStrikeInLastFrame() {
        // Arrange
        frame = new Frame(generateur, true);
        when(generateur.randomPin(10)).thenReturn(10, 5);

        // Act
        frame.makeRoll();
        frame.makeRoll();

        // Assert
        assertEquals(15, frame.getScore());
    }

    @Test
    void shouldAcceptThirdRollWhenLastFrameStartsWithStrike() {
        // Arrange
        frame = new Frame(generateur, true);
        when(generateur.randomPin(10)).thenReturn(10, 3, 2);

        // Act
        assertTrue(frame.makeRoll());
        assertTrue(frame.makeRoll());
        boolean thirdRollAccepted = frame.makeRoll();

        // Assert
        assertTrue(thirdRollAccepted);
    }

    @Test
    void shouldAcceptSecondRollWhenLastFrameStartsWithStrike() {
        // Arrange
        frame = new Frame(generateur, true);
        when(generateur.randomPin(10)).thenReturn(10, 5);

        // Act
        boolean firstRollAccepted = frame.makeRoll();
        boolean secondRollAccepted = frame.makeRoll();

        // Assert
        assertTrue(firstRollAccepted);
        assertTrue(secondRollAccepted);
    }

    @Test
    void shouldIncreaseScoreWhenThirdRollIsMadeAfterStrikeInLastFrame() {
        // Arrange
        frame = new Frame(generateur, true);
        when(generateur.randomPin(10)).thenReturn(10, 3, 4);

        // Act
        frame.makeRoll();
        frame.makeRoll();
        frame.makeRoll();

        // Assert
        assertEquals(17, frame.getScore());
    }

    @Test
    void shouldAcceptThirdRollWhenLastFrameStartsWithSpare() {
        // Arrange
        frame = new Frame(generateur, true);
        when(generateur.randomPin(10)).thenReturn(7, 2);
        when(generateur.randomPin(3)).thenReturn(3);

        // Act
        frame.makeRoll();
        frame.makeRoll();
        boolean thirdRollAccepted = frame.makeRoll();

        // Assert
        assertTrue(thirdRollAccepted);
    }

    @Test
    void shouldIncreaseScoreWhenThirdRollIsMadeAfterSpareInLastFrame() {
        // Arrange
        frame = new Frame(generateur, true);
        when(generateur.randomPin(10)).thenReturn(7, 5);
        when(generateur.randomPin(3)).thenReturn(3);

        // Act
        frame.makeRoll();
        frame.makeRoll();
        frame.makeRoll();

        // Assert
        assertEquals(15, frame.getScore());
    }

    @Test
    void shouldRejectThirdRollWhenLastFrameHasNoStrikeOrSpare() {
        // Arrange
        frame = new Frame(generateur, true);
        when(generateur.randomPin(10)).thenReturn(3);
        when(generateur.randomPin(7)).thenReturn(4);

        // Act
        frame.makeRoll();
        frame.makeRoll();
        boolean thirdRollAccepted = frame.makeRoll();

        // Assert
        assertFalse(thirdRollAccepted);
        assertEquals(7, frame.getScore());
    }

    @Test
    void shouldRejectFourthRollInLastFrame() {
        // Arrange
        frame = new Frame(generateur, true);
        when(generateur.randomPin(10)).thenReturn(10, 3, 4);

        // Act
        frame.makeRoll();
        frame.makeRoll();
        frame.makeRoll();
        boolean fourthRollAccepted = frame.makeRoll();

        // Assert
        assertFalse(fourthRollAccepted);
        assertEquals(17, frame.getScore());
    }
}
