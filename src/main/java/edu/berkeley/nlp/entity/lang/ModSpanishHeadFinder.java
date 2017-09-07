package edu.berkeley.nlp.entity.lang;

//import edu.berkeley.nlp.ling.CategoryWordTag;

import edu.berkeley.nlp.futile.ling.AbstractCollinsHeadFinder;
import edu.berkeley.nlp.futile.syntax.Tree;
import edu.berkeley.nlp.futile.syntax.Trees;
import edu.berkeley.nlp.futile.treebank.PennTreebankLanguagePack;
import edu.berkeley.nlp.futile.treebank.TreebankLanguagePack;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;

/**
 * Implements the HeadFinder found in Michael Collins' 1999 thesis. Except:
 * we've added a head rule for NX, which returns the leftmost item. No rule for
 * the head of NX is found in any of the versions of Collins' head table that we
 * have (did he perhaps use the NP rules for NX? -- no Bikel, CL, 2005 says it
 * defaults to leftmost). These rules are suitable for the Penn Treebank.
 * <p>
 * May 2004: Added support for AUX and AUXG to the VP rules; these cause no
 * interference in Penn Treebank parsing, but means that these rules also work
 * for the BLLIP corpus (or Charniak parser output in general). Feb 2005: Fixes
 * to coordination reheading so that punctuation cannot become head.
 *
 * @author Christopher Manning
 */

public class ModSpanishHeadFinder extends AbstractCollinsHeadFinder {

	static String[] leftExceptPunct = {"left"};
	static String[] rightExceptPunct = {"right"};

	public ModSpanishHeadFinder() {
		this(new PennTreebankLanguagePack());
	}

	protected int postOperationFix(int headIdx, List<Tree<String>> daughterTrees) {
		if (headIdx >= 2) {
			String prevLab = daughterTrees.get(headIdx - 1).getLabel();
			if (prevLab.startsWith("CC") || prevLab.equals("COORD") || prevLab.equals("CONJ")) {
				int newHeadIdx = headIdx - 2;
				Tree<String> t = daughterTrees.get(newHeadIdx);
				while (newHeadIdx >= 0 && t.isPreTerminal()
						&& tlp.isPunctuationTag(t.getLabel())) {
					newHeadIdx--;
				}
				if (newHeadIdx >= 0) {
					headIdx = newHeadIdx;
				}
			}
		}
		return headIdx;
	}

		@SuppressWarnings("unchecked")
	public ModSpanishHeadFinder(TreebankLanguagePack tlp) {
		super(tlp);
			defaultRule = new String[] { "right" };

			nonTerminalInfo = new HashMap();

			nonTerminalInfo.put("SN", new String[][]{
					{"rightdis", "^AQA.*", "^AQC.*", "^GRUP\\.A", "^S\\.A",	"^NC.*S.*",	"^NP.*", "^NC.*P.*", "^GRUP\\.NOM"},
					{"left", "^SN", "^GRUP\\.NOM"},
					{"rightdis", "\\$", "^GRUP\\.A", "^S\\.A", "^SA"},
					{"right", "^Z.*"},
					{"rightdis", "^AQ0.*","^AQ[AC].*","^AO.*","^GRUP\\.A","^S\\.A","^RG","^RN","^GRUP\\.NOM"}});
			nonTerminalInfo.put("GRUP.NOM", new String[][]{
					{"rightdis", "^AQA.*", "^AQC.*", "^GRUP\\.A", "^S\\.A", "^NC.*S.*", "^NP.*", "^NC.*P.*", "^GRUP\\.NOM"},
					{"left", "^SN", "^GRUP\\.NOM"},
					{"rightdis", "\\$", "^GRUP\\.A", "^S\\.A", "^SA"},
					{"right", "^Z.*"},
					{"rightdis", "^AQ0.*", "^AQ[AC].*", "^AO.*", "^GRUP\\.A", "^S\\.A", "^RG", "^RN", "^GRUP\\.NOM"}});


			nonTerminalInfo.put("SENTENCE", new String[][] {
				{"left","^PREP", "^SP[CS].*", "^CS.*", "^GRUP\\.VERB", "^S", "^SA", "^COORD", "^CONJ", "^GRUP\\.NOM",
						"^SN","^S"}});
			nonTerminalInfo.put("S", new String[][]{
					{"left","^PREP", "^SP[CS].*", "^COORD", "^CONJ", "^CS.*", "^GRUP\\.VERB", "^S", "^SA", "^COORD",
							"^GRUP\\.NOM", "^SN"}});
			nonTerminalInfo.put("SA", new String[][]{
					{"left", "^NC.*P.*", "^GRUP\\.NOM", "\\$", "^NC.*S.*", "^SADV", "^GRUP\\.ADV", "^AQA.*", "^AQC.*",
							"^V[MAS]P.*", "^V[MAS]G.*", "^SA", "^S\\.A", "^GRUP\\.A", "^AQS.*", "^SN", "^GRUP\\.NOM", "^D.*",
							"^S", "^RG", "^RN"}} );
			nonTerminalInfo.put("S.A", new String[][]{
					{"left", "^NC.*P.*", "^GRUP\\.NOM", "\\$", "^NC.*S.*", "^SADV", "^GRUP\\.ADV", "^AQA.*", "^AQC.*",
							"^V[MAS]P.*", "^V[MAS]G.*", "^S\\.A", "^GRUP\\.A", "^AQS.*", "^SN", "^GRUP\\.NOM", "^D.*", "^S",
							"^RG", "^RN"}} );
			nonTerminalInfo.put("SADV", new String[][]{
					{"right","^S", "^RG", "^RN", "^SADV", "^GRUP\\.ADV", "^SP[CS].*", "^PREP", "^Z.*", "^AQA.*", "^AQC.*",
							"^S\\.A", "^GRUP\\.A", "^CONJ", "^CS.*", "^SN", "^GRUP\\.NOM", "^AQS.*", "^NC.*S.*"}});
			nonTerminalInfo.put("SP", new String[][]{
					{"right", "^SP[CS].*", "^PREP", "^CS.*", "^CONJ", "^V[MAS]G.*", "^V[MAS]P.*"}});
			nonTerminalInfo.put("GRUP.A",new String[][]{
					{"left","^NC.*P.*", "^GRUP\\.NOM","\\$", "^NC.*S.*", "^SADV", "^GRUP\\.ADV", "^AQA.*", "^AQC.*",
							"^V[MAS]P.*", "^V[MAS]G.*", "^GRUP\\.A", "^AQS.*", "^SN", "^GRUP\\.NOM", "^D.*", "^S",
							"^RG", "^RN"}} );
			nonTerminalInfo.put("GRUP.ADV",new String[][]{
					{"right", "^RG", "^RN", "^GRUP\\.ADV", "^PREP", "^SP.*", "^Z.*", "^AQA.*", "^AQC.*", "^GRUP\\.A",
							"^S\\.A","^CS.*", "^CONJ","^SN", "^GRUP\\.NOM", "^AQS.*", "^NC.*S.*"}});
			nonTerminalInfo.put("GRUP.VERB", new String[][]{
					{"left", "^INFINITIU", "^GERUNDI", "^PARTICIPI", "^PREP", "^SP[CS].*", "^V[MAS].*[IS].*", "^V[MAS]P.*",
							"^V.*C.*", "^V[MAS]IP3S.*", "^V.*", "^V[MAS]G.*", "^V[MAS]IP[12]S.*","^GRUP\\.VERB",
							"^SA", "^S\\.A", "^GRUP\\.A", "^NC.*S.*", "^NC.*P.*", "^GRUP\\.NOM","^SN","^S"}});
			nonTerminalInfo.put("INFINITIU", new String[][]{
					{"left", "^VMN.*", "^V[MAS]N.*", "^V.*"}});
			nonTerminalInfo.put("GERUNDI", new String[][]{
					{"left", "^VMG.*", "^V[MAS]G.*", "^V.*"}});
			nonTerminalInfo.put("PARTICIPI", new String[][]{
					{"left", "^VMP.*", "^V[MAS]P.*", "^V.*"}});
			nonTerminalInfo.put("MORFEMA.PRONOMINAL", new String[][]{
					{"left", "^P.*", "^SN.*", "^GRUP\\.NOM.*", "^GRUP\\.VERB"}});
			nonTerminalInfo.put("MORFEMA.VERBAL", new String[][]{
					{"left", "^GRUP\\.VERB", "^P.*", "^SN.*", "^GRUP\\.NOM.*", "^S"}});
			nonTerminalInfo.put("COORD", new String[][]{
					{"right", "^CONJ", "^CC.*", "^RB", "^RN","^SP[CS].*", "^PREP", "^CS"}});
			nonTerminalInfo.put("CONJ", new String[][]{
					{"right", "^CONJ", "^CC.*", "^RB", "^RN","^SP[CS].*", "^PREP", "^CS", "^[^F]"}});
			nonTerminalInfo.put("INC",new String[][]{
					{"left","^S", "^SN", "^GRUP\\.NOM", "^GRUP\\.VERB", "^SADV", "^GRUP.ADV", "^SA", "^S\\.A", "^GRUP\\.A",
							"^PREP", "^SP[CS].*", "^CONJ", "^CS", "^D.*"}});
			nonTerminalInfo.put("INTERJECCIO", new String[][]{
					{"left", "^I"}});
			nonTerminalInfo.put("NEG", new String[][]{
					{"left", "^RN"}});

			nonTerminalInfo.put("PREP", new String[][]{
					{"left", "^PREP", "^SP[CS].*", "^CONJ", "^CS$"}});

			nonTerminalInfo.put("RELATIU", new String[][]{
					{"left", "^P.*", "^SN", "^GRUP\\.NOM", "^S$", "^GRUP\\.VERB"}});

			nonTerminalInfo.put("SPEC", new String[][]{
					{"left"}});
			nonTerminalInfo.put("X", new String[][]{
					{"right"}});

			nonTerminalInfo.put("TOP", new String[][]{
					{ "left" } });
	}

	/**
	 * Go through trees and determine their heads and print them. Just for
	 * debuggin'. <br>
	 * Usage: <code>
	 * java edu.stanford.nlp.trees.CollinsHeadFinder treebankFilePath
	 * </code>
	 *
	 * @param args
	 *          The treebankFilePath
	 */

	public static void main(String[] args) {
		Trees.PennTreeReader reader = new Trees.PennTreeReader(new StringReader("(TOP (SENTENCE(PATATA EL (GRUP.NOM o (SP(PREP lo)(PATATA (GRUP.NOM x)))(PATTA (GRUP.NOM  el )  el )))(GRUP.VERB  el )(SP(PREP  el )(PATTA(GRUP.NOM(S.A(GRUP.A  el ))  el (S.A(GRUP.A  el    el    el )) (SN(GRUP.NOM  el ))))) (SN  el  (GRUP.NOM  el (SP(PREP  el )(SN(GRUP.NOM(GRUP.NOM  el ) (CONJ  el ) (GRUP.NOM  el (SP(PREP  el )(SN(GRUP.NOM  el )))))))))  el ))"));
		Tree<String> tree = reader.next();
		System.out.println("tree "+tree);

		ModSpanishHeadFinder headFinder = new ModSpanishHeadFinder();
		while (!tree.isLeaf()) {
			Tree<String> head = headFinder.determineHead(tree);
			System.out.println("head "+head);
			tree=head;
		}
	}

	private static final long serialVersionUID = -8747319554557223437L;

}


