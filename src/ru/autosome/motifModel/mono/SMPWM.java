package ru.autosome.motifModel.mono;

import ru.autosome.Assistant;
import ru.autosome.motifModel.PWM;
import ru.autosome.sequenceModel.mono.SMSequence;

import static ru.autosome.utils.with_zero_row;

public class SMPWM implements PWM<SMPWM> {
    public final double[][] matrix;
    private final int motifLength;
    SMPWM(double[][] matrix, int motifLength) {
        this.matrix = matrix;
        this.motifLength = motifLength;
    }

    public double score(SMSequence seq, int position) {
        double score = 0.0;
        for (int k = 0; k < this.matrix.length; k++) {
            byte letter = seq.sequence[position + 2 * k];
            score += this.matrix[k][letter];
        }
        return score;
    }

    public static SMPWM fromNaive(MPWM original_mpwm) {
        double[][] sup_matrix;
        double[][] matrix = original_mpwm.matrix;

        if (matrix.length % 2 == 0) {
            sup_matrix = new double[matrix.length / 2][25];
            makeSMMatrixFromMMatrix(matrix, sup_matrix);
        } else {
            sup_matrix = new double[matrix.length / 2 + 1][25];
            makeSMMatrixFromMMatrix(with_zero_row(matrix), sup_matrix);
        }

        return new SMPWM(sup_matrix, original_mpwm.motif_length());
    }

    static void makeSMMatrixFromMMatrix(double[][] matrix, double[][] sup_matrix) {
        for (int l = 0; l < matrix.length / 2; l++) {
            int k = 0, j = 0;
            for (int n = 0; n < 25; n++) {
                if (n > 0 && n % 5 == 0) {
                    k += 1;
                    j = 0;
                }
                sup_matrix[l][n] = matrix[2 * l][k] + matrix[2 * l + 1][j];
                j++;
            }
        }

    }

    @Override
    public SMPWM revcomp() {
        double[][] matrix = this.matrix;
        double[][] new_matrix = new double[matrix.length][25];
        for (int k = 0; k < matrix.length; k++) {
            for (int j = 0; j < 25; j++) {
                new_matrix[matrix.length - 1 - k][j] = matrix[k][Assistant.dComplimentaryElements[j]];
            }
        }
        return new SMPWM(new_matrix, motifLength);
    }

    @Override
    public int motif_length() {
        return motifLength;
    }
}
