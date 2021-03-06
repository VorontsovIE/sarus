package ru.autosome.scanningModel;

import ru.autosome.motifModel.di.SuperAlphabetDPWM;
import ru.autosome.sequenceModel.di.SuperAlphabetDSequence;

public class SuperAlphabetDPWMScanner extends SequenceScanner {
    public static class Builder extends SequenceScanner.Builder<SuperAlphabetDPWM> {
        public Builder(SuperAlphabetDPWM motif, boolean scanDirect, boolean scanRevcomp) {
            super(motif, scanDirect, scanRevcomp);
        }
        public SuperAlphabetDPWMScanner scannerForSequence(String str)  {
            return new SuperAlphabetDPWMScanner(motif, SuperAlphabetDSequence.sequenceFromString(str), scanDirect, scanRevcomp);
        }
    }

    private final SuperAlphabetDPWM motif, revcomp_motif;
    private final SuperAlphabetDSequence sequence;
    public SuperAlphabetDPWMScanner(SuperAlphabetDPWM motif, SuperAlphabetDSequence sequence, boolean scanDirect, boolean scanRevcomp) {
        super(scanDirect, scanRevcomp);
        this.motif = motif;
        this.revcomp_motif = motif.revcomp();
        this.sequence = sequence;
    }

    @Override double direct_score(int position) { return motif.score(sequence, position); }
    @Override double revcomp_score(int position) { return revcomp_motif.score(sequence, position); }

    @Override public int scanningStartIndex() { return 1; }
    @Override public int scanningEndIndex() {
        if (motif.motif_length() % 2 == 0) {
            return sequence.sequence.length - 2 * motif.matrix.length + 2;
        } else {
            return sequence.sequence.length - 2 * motif.matrix.length + 1;
        }
    }
    @Override public int shiftForRevcompScore() {
        if (motif.motif_length() % 2 == 0) {
            return -1;
        } else {
            return 0;
        }
    }
    @Override public int shiftForPrint() { return -1; }
}
