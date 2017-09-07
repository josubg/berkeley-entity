package edu.berkeley.nlp.entity.coref
import scala.collection.mutable.HashMap

object PronounDictionary {
  val firstPersonPronouns = Set("conmigo", "me", "me", "mía", "mías", "mí", "mío", "mío", "míos", "nos", "nos", "nosotras",
                                                             "nosotros", "nuestra", "nuestras", "nuestro", "nuestro", "nuestros", "yo");
  val secondPersonPronouns = Set("contigo", "os", "os", "te", "te", "ti", "tú", "tuyas", "tuya", "tuyos", "tuyo", "tuyo",
                                                               "ustedes", "usted", "vosotras", "vosotros", "vos", "vuestras", "vuestra", "vuestros",
                                                               "vuestro", "vuestro");
  val thirdPersonPronouns = Set("consigo", "él", "ella", "ellas", "ellos", "la", "las", "le", "les", "lo", "lo", "los",
                                                             "se", "se", "sí", "suyas", "suya", "suyos", "suyo", "suyo");
  val otherPronouns = Set("algo", "alguien", "alguna", "algunas", "alguno", "algunos", "ambas", "ambos", "bastante",
                                                     "bastantes", "cual", "cualesquiera", "cualquiera", "demás", "demasiada", "demasiadas",
                                                     "demasiado", "demasiados", "media", "medias", "medio", "medios", "misma", "mismas", "mismo",
                                                     "mismos", "mucha", "muchas", "mucho", "muchos", "nada", "nadie", "naide", "ninguna",
                                                     "ningunas", "ninguno", "ningunos", "otra", "otras", "otro", "otros", "poca", "pocas", "poco",
                                                     "pocos", "quienesquiera", "quienquiera", "tantas", "tanta", "tantos", "tanto", "todas",
                                                     "toda", "todos", "unas", "una", "unos", "uno", "varias", "varios", "todo");
  
  val demonstratives = Set("este", "ese","aquel", "esta", "esa", "aquella", "estos", "esos", "aquellos", "estas", "esas", "aquellas");
  
  // Borrowed from Stanford
  val singularPronouns = Set("conmigo", "contigo", "él", "ella", "la", "le", "lo", "me", "mía", "mí", "mío", "nuestra",
                                                      "nuestro", "nuestro", "suya", "suyo", "suyo", "te", "ti", "tú", "tuya", "tuyo", "tuyo",
                                                      "usted", "vos", "vuestra", "vuestro", "vuestro", "yo");
  val pluralPronouns = Set("ellas", "ellos", "las", "les", "los", "mías", "míos", "nos", "nosotras", "nosotros", "nuestras",
                                                  "nuestros", "os", "suyas", "suyos", "tuyas", "tuyos", "ustedes", "vosotras", "vosotros",
                                                  "vuestras", "vuestros");
  val malePronouns = Set("él", "ellos", "lo", "los", "mío", "míos", "nosotros", "nuestro", "nuestros", "suyos", "suyo",
                                              "tuyos", "tuyo", "vosotros", "vuestros", "vuestro");
  val femalePronouns = Set("ella", "ellas", "la", "las", "mía", "mías", "nosotras", "nuestra", "nuestras", "suyas", "suya",
                                                  "tuyas", "tuya", "vosotras", "vuestras", "vuestra");
  val neutralPronouns = Set("conmigo", "consigo", "contigo", "le", "les", "lo", "me", "mía", "mío", "nos", "nuestro", "os",
                                                    "se", "sí", "suyo", "te", "ti", "tú", "tuyo", "ustedes", "usted", "vos", "vuestro", "yo");
  
  
  val allPronouns = firstPersonPronouns ++ secondPersonPronouns ++ thirdPersonPronouns ++ otherPronouns;

  // Constructed based on Stanford"s Dictionaries class
  val canonicalizations = new HashMap[String,String]();
  var forms = Set("conmigo", "me", "me", "mía", "mías", "mí", "mío", "mío", "míos, yo")
  for( x <- forms ){
   canonicalizations.put(x, "i");
  }

  forms = Set("nos", "nos", "nosotras", "nosotros", "nuestra", "nuestras", "nuestro", "nuestro", "nuestros")
  for( x <- forms ){
      canonicalizations.put(x, "we");
  }
  forms = Set("contigo", "os", "os", "te", "te", "ti", "tú", "tuyas", "tuya", "tuyos", "tuyo", "tuyo",
       "ustedes", "usted", "vosotras", "vosotros", "vos", "vuestras", "vuestra", "vuestros", "vuestro", "vuestro")
  for( x <- forms ){
      canonicalizations.put(x, "you");
  }
  var x = 0;
  forms = Set("él", "suyo")
  for( x <- forms ){
    canonicalizations.put(x, "he");
  }
  forms = Set("ella", "la", "suya")
  for( x <- forms ){
     canonicalizations.put(x, "she");
  }

  forms = Set("ellas", "ellos", "las", "les", "los", "suyas","suyos")
  for( x <- forms ){
     canonicalizations.put(x, "they");
  }

  forms = Set("lo")
  for( x <- forms ){
     canonicalizations.put(x, "it");
  }

  forms = Set("esto", "este", "esta")
  for( x <- forms ){
       canonicalizations.put(x, "this");
  }

  forms = Set("eso", "ese", "esa","aquello", "aquella", "aquel")
  for( x <- forms ){
      canonicalizations.put(x, "that");
  }
  forms = Set("estas", "estos")
  for( x <- forms ){
      canonicalizations.put(x, "these");
  }
  forms = Set("esos", "esas","aquellos", "aquellas")
  for( x <- forms ){
      canonicalizations.put(x, "those");
  }
  forms = Set("cual", "cuales")
  for( x <- forms ){
        canonicalizations.put(x, "which");
  }
  forms = Set("quien", "quienes")
  for( x <- forms ){
        canonicalizations.put(x, "who");
  }


  //canonicalizations.put("donde", "where");
  //canonicalizations.put("cuyos", "whose");
  // This entry is here just to make results consistent with earlier ones
  // on our very small dev set
  //canonicalizations.put("thy", "thy");
  //canonicalizations.put("y'all", "you");
  //canonicalizations.put("you're", "you");
  //canonicalizations.put("you'll", "you");
  //canonicalizations.put("'s", "'s");
  
  def isPronLc(str: String): Boolean = {
    !mightBeAcronym(str) && allPronouns.contains(str.toLowerCase());
  }
  
  def isDemonstrative(str: String): Boolean = {
    !mightBeAcronym(str) && demonstratives.contains(str.toLowerCase());
  }
  
  def mightBeAcronym(str: String) = {
    if (str.size <= 4) {
      var acronym = true;
      var i = 0;
      while (acronym && i < str.size) {
        if (!Character.isUpperCase(str.charAt(i))) {
          acronym = false;
        }
        i += 1;
      }
      acronym;
    } else {
      false;
    }
  }
  
  def canonicalize(str: String): String = {
    if (!canonicalizations.contains(str.toLowerCase())) {
      "";
    } else {
      canonicalizations(str.toLowerCase());
    }
  }
  
  def main(args: Array[String]) {
    println(PronounDictionary.canonicalizations("'em"));
    println(PronounDictionary.isPronLc("them"));
    println(PronounDictionary.isPronLc("Them"));
    println(PronounDictionary.isPronLc("NotThem"));
    println(PronounDictionary.mightBeAcronym("them"));
    println(PronounDictionary.mightBeAcronym("Them"));
    println(PronounDictionary.mightBeAcronym("THEM"));
  }
}
