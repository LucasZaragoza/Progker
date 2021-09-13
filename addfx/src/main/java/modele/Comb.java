package modele;

public enum Comb {
	 HC(1), P(2), DP(3), TOK(4), STRAIGHT(5), FLUSH(6), FH(7), FOK(8), SF(9);

    private int force;

    Comb(int nb) {
        force = nb;
    }

    public int getforce() {
        return force;
    }
    
}
