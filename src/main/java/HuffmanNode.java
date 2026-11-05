public class HuffmanNode {

  int verdi;
  int frekvens;
  HuffmanNode venstre;
  HuffmanNode høyre;

  HuffmanNode(int verdi, int frekvens) {
    this.verdi = verdi;
    this.frekvens = frekvens;
  }

  HuffmanNode(HuffmanNode venstre, HuffmanNode høyre) {
    this.venstre = venstre;
    this.høyre = høyre;
    this.frekvens = venstre.frekvens + høyre.frekvens;
  }

  boolean erBlad() {
    return venstre == null;
  }
}
