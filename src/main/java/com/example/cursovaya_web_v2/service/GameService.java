package com.example.cursovaya_web_v2.service;

import com.example.cursovaya_web_v2.data.Chip;
import com.example.cursovaya_web_v2.data.Chips;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class GameService {

    @Getter
    private Chips matrix = new Chips();

    @PostConstruct
    public void init() {
        matrix = new Chips(fillMatrix());
    }

    public void refreshMatrix() {
        matrix = new Chips(fillMatrix());
    }

    private Chip[][] fillStdMatrix() {
        Chip[][] tempMatrix = new Chip[4][4];
        int index = 1;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tempMatrix[i][j] = new Chip(i, j, index++);
            }
        }
        return tempMatrix;
    }

    private Chip[][] fillMatrix() {
        Chip[][] tempMatrix = fillStdMatrix();
        Chip sixteenthChip = tempMatrix[3][3];
        for (int i = 0; i < 1000; i++) {
            Chip neighborChip = getRandomNeighborChip(tempMatrix,sixteenthChip);
            if (!(neighborChip == sixteenthChip)) {
                swap(tempMatrix, sixteenthChip, neighborChip);
                sixteenthChip = neighborChip;
            }
        }
        return tempMatrix;
    }

    private Chip findSixteenthChip(Chip[][] tempMatrix, int x, int y) {
        int[][] positions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] position : positions) {
            int tempX = x + position[0];
            int tempY = y + position[1];
            if (tempX >= 0 && tempX < 4 && tempY >= 0 && tempY < 4) {
                Chip chip = tempMatrix[tempX][tempY];
                if (chip.getValue() == 16) {
                    return chip;
                }
            }
        }
        return null;
    }

    public void clickAndCheckChip(int x, int y) {
        Chip[][] tempMatrix = matrix.getMatrix();
        Chip selectedChip = tempMatrix[x][y];
        Chip sixteenthChip = findSixteenthChip(tempMatrix, x, y);
        if (sixteenthChip != null) {
            swap(tempMatrix, sixteenthChip, selectedChip);
            checkWin();
        }
    }

    private void swap(Chip[][] tempMatrix, Chip sixteenthChip, Chip selectedChip) {
        int tempValue = sixteenthChip.getValue();
        sixteenthChip.setValue(selectedChip.getValue());
        selectedChip.setValue(tempValue);

        tempMatrix[sixteenthChip.getX()][sixteenthChip.getY()] = sixteenthChip;
        tempMatrix[selectedChip.getX()][selectedChip.getY()] = selectedChip;

        matrix.setMatrix(Arrays.copyOf(tempMatrix, tempMatrix.length));

    }

    private void checkWin() {
        Chips winMatrix = new Chips(fillStdMatrix());
        if (matrix.equals(winMatrix)) {
            System.out.println("WIN");
        }
    }

    private Chip getRandomNeighborChip(Chip[][] tempMatrix, Chip sixteenthChip) {
        int x = sixteenthChip.getX();
        int y = sixteenthChip.getY();

        List<Chip> neighborChips = new ArrayList<>();
        if (x > 0) {
            neighborChips.add(tempMatrix[x - 1][y]);
        }
        if (x < 3) {
            neighborChips.add(tempMatrix[x + 1][y]);
        }
        if (y > 0) {
            neighborChips.add(tempMatrix[x][y - 1]);
        }
        if (y < 3) {
            neighborChips.add(tempMatrix[x][y + 1]);
        }

        Random random = new Random();
        int index = random.nextInt(neighborChips.size());
        return neighborChips.get(index);
    }
}