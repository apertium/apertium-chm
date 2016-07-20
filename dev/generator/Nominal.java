import java.util.ArrayList;

public class Nominal {

	private String lemma;

	private Stress stress;

	private Number number;

	private WordCase wordCase;

	private PosessiveSuffix posSuffix;

	private ArrayList<Enclitic> enclitics;

	private boolean extra;

	private int derivation;

	private int classOver;

	private boolean compDegreeN;

	private boolean pers3Sings;

	private boolean sEnclitic;

	private boolean andParticle;

	private boolean strengthParticle;
	
	private boolean reduplication;

	/**
	 * 
	 * @param lemma
	 * @param stress
	 * @param number
	 * @param wordCase
	 * @param posSuffix
	 * @param enclitics
	 * @param extra
	 */
	public Nominal(String lemma, Stress stress, Number number, WordCase wordCase,
			PosessiveSuffix posSuffix, ArrayList<Enclitic> enclitics,
			boolean extra) {
		
		this.lemma = lemma;
		this.stress = stress;
		this.number = number;
		this.wordCase = wordCase;
		this.posSuffix = posSuffix;
		this.enclitics = enclitics;
		this.extra = extra;
		
		for (Enclitic enclitic : enclitics) {
			if (enclitic instanceof SEnclitic)
				this.sEnclitic = true;
			if (enclitic instanceof CompDegEnclitic)
				this.compDegreeN = true;
			if (enclitic instanceof AndEnclitic)
				this.andParticle = true;
			if (enclitic instanceof ThirdPersonSingEnclitic)
				this.pers3Sings = true;
			if (enclitic instanceof StrengtheningEnclitic)
				this.strengthParticle = true;
			if (enclitic instanceof ReduplicationEnclitic)
				this.reduplication = true;
		}
	}

	public String getSurfaceForm() {
		
		if (!isValid())
			return null;
		
		String out = morph.toC(morph.toLower(this.lemma));

		int sSt = this.getStress().toInt();

		if ((this.isExtra()) && (morph.pronoun(lemma))) {
			out = morph.pronoun(lemma, 2, 0, this.getPosSuffix().toInt());
		} else if (this.isExtra()) {
			out = morph.genitive(out, this.getStress().toInt());
		}
		if (morph.pronoun(out)) {
			out = morph.pronoun(out, this.getWordCase().toInt(), this
					.getPosSuffix().toInt(), this.getPosSuffix().toInt());
		} else if ((this.getDerivation() > 4) && (this.getDerivation() < 10)) {
			out = morph.derr(out, this.getStress().toInt(), this.getDerivation(),
					this.getClassOver());
			if (this.isCompDegreeN()) {
				out = morph.compDegree(out, this.getStress().toInt());
			}
		} else if (this.getWordCase().toInt() <= 6) {
			out = morph.underClass6(out, sSt, this.getPosSuffix().toInt(), this
							.getDerivation(), this.isCompDegreeN(), this
							.getNumber().toInt(), this.getStress().toInt(), this
							.getClassOver(), this.isExtra(), this.pers3Sings,
							this.getWordCase().toInt());
		} else {
			out = morph.derr(out, this.getStress().toInt(), this.getDerivation(),
					this.getClassOver());
			if ((this.getDerivation() != 1) && (this.isCompDegreeN())) {
				out = morph.compDegree(out, 0);
			} else if (this.isCompDegreeN()) {
				out = morph.compDegree(out, this.getStress().toInt());
			}
			int cSt = this.getStress().toInt();
			int pSt = this.getStress().toInt();
			int nSt = this.getStress().toInt();
			if ((this.isCompDegreeN()) || (this.getDerivation() != 1)) {
				pSt = 0;
				cSt = 0;
				sSt = 0;
				nSt = 0;
			} else if (this.getNumber().toInt() != 1) {
				pSt = 0;
				cSt = 0;
				sSt = 0;
			} else if (this.getWordCase().toInt() != 1) {
				pSt = 0;
				sSt = 0;
			} else if (this.getPosSuffix().toInt() != 0) {
				sSt = 0;
			}
			out = morph.possessive(morph.caseEnding(morph.number(out, nSt, this
					.getNumber().toInt(), this.getPosSuffix().toInt()), cSt,
					this.getWordCase().toInt(), this.getPosSuffix().toInt()),
					pSt, this.getPosSuffix().toInt(), this.pers3Sings);
		}
		 if (this.reduplication) {
		 out = morph.redup(out, sSt);
		 }
		 if (this.andParticle) {
		 out = morph.also(out, sSt);
		 }
		 if (this.strengthParticle) {
		 out = morph.strong(out, sSt);
		 }
		 if (this.sEnclitic) {
		 out = morph.sEnclit(out, sSt);
		 }
		out = morph.replace(out, "$а", "я");
		out = morph.replace(out, "$у", "ю");
		out = morph.replace(out, "$э", "е");
		out = morph.replace(out, "$е", "е");
		out = morph.replace(out, "$и", "и");

		out = morph.replace(out, "$", "ь");

		return out;
	}

	private boolean isValid() {

		
		
		return true;
	}

	public boolean isExtra() {
		return extra;
	}

	public void setExtra(boolean extra) {
		this.extra = extra;
	}

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public Stress getStress() {
		return stress;
	}

	public void setStress(Stress stress) {
		this.stress = stress;
	}

	public Number getNumber() {
		return number;
	}

	public void setNumber(Number number) {
		this.number = number;
	}

	public WordCase getWordCase() {
		return wordCase;
	}

	public void setWordCase(WordCase wordCase) {
		this.wordCase = wordCase;
	}

	public PosessiveSuffix getPosSuffix() {
		return posSuffix;
	}

	public void setPosSuffix(PosessiveSuffix posSuffix) {
		this.posSuffix = posSuffix;
	}

	public ArrayList<Enclitic> getEnclitics() {
		return enclitics;
	}

	public void setEnclitics(ArrayList<Enclitic> enclitics) {
		this.enclitics = enclitics;
	}

	public String getLexicalForm() {
		return getLemma() + this.getTags() + "/" + this.getSurfaceForm();
	}

	public String getTags() {
		
		// Tag order: pos.nbr.case.poss.stress.enc
		String tags = number.getTag() + wordCase.getTag() + posSuffix.getTag() + stress.getTag();
		
		if (extra)
			tags += "<extra>";

		
		String encliticTags = "";
		for (Enclitic enclitic : enclitics)
			if (enclitic instanceof Enclitic) // Check nulls
				encliticTags += enclitic.getTag();
		
		tags += encliticTags;
		
		return tags;
	}

	public int getDerivation() {
		return derivation;
	}

	public void setDerivation(int derivation) {
		this.derivation = derivation;
	}

	public int getClassOver() {
		return classOver;
	}

	public void setClassOver(int classOver) {
		this.classOver = classOver;
	}

	public boolean isCompDegreeN() {
		return compDegreeN;
	}

	public void setCompDegreeN(boolean compDegreeN) {
		this.compDegreeN = compDegreeN;
	}
}
