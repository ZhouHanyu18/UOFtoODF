package temp_structs;

public class Default_Styles {
	public static final String _graphic =
		"<style:default-style style:family=\"graphic\">"
		+ "<style:graphic-properties draw:shadow-offset-x=\"8.5pt\" draw:shadow-offset-y=\"8.5pt\" draw:start-line-spacing-horizontal=\"8.02pt\" draw:start-line-spacing-vertical=\"8.02pt\" draw:end-line-spacing-horizontal=\"8.02pt\" draw:end-line-spacing-vertical=\"8.02pt\" style:flow-with-text=\"false\" />"
	    + "<style:paragraph-properties style:text-autospace=\"ideograph-alpha\" style:line-break=\"strict\" style:writing-mode=\"lr-tb\" style:font-independent-line-spacing=\"false\">"
	    + "<style:tab-stops />"
	    + "</style:paragraph-properties>"
	    + "<style:text-properties style:use-window-font-color=\"true\" fo:font-size=\"12pt\" fo:language=\"en\" fo:country=\"US\" style:font-size-asian=\"12pt\" style:language-asian=\"zh\" style:country-asian=\"CN\" style:font-size-complex=\"12pt\" style:language-complex=\"none\" style:country-complex=\"none\" />"
	  + "</style:default-style>";

	public static final String _paragraph =
		"<style:default-style style:family=\"paragraph\">"
	    + "<style:paragraph-properties fo:hyphenation-ladder-count=\"no-limit\" style:text-autospace=\"ideograph-alpha\" style:punctuation-wrap=\"hanging\" style:line-break=\"strict\" style:tab-stop-distance=\"35.29pt\" style:writing-mode=\"page\" />"
	    + "<style:text-properties style:use-window-font-color=\"true\" style:font-name=\"Times New Roman\" fo:font-size=\"12pt\" fo:language=\"en\" fo:country=\"US\" style:font-name-asian=\"宋体\" style:font-size-asian=\"12pt\" style:language-asian=\"zh\""
	    + " style:country-asian=\"CN\" style:font-name-complex=\"Tahoma\" style:font-size-complex=\"12pt\" style:language-complex=\"none\" style:country-complex=\"none\" fo:hyphenate=\"false\" fo:hyphenation-remain-char-count=\"2\" fo:hyphenation-push-char-count=\"2\" />"
	  + "</style:default-style>";

	public static final String _table =
		"<style:default-style style:family=\"table\">"
	    + "<style:table-properties table:border-model=\"collapsing\" />"
	  + "</style:default-style>";

	public static final String _table_cell =
		"<style:default-style style:family=\"table-cell\">"
		+ "<style:table-cell-properties style:decimal-places=\"2\" />"
	    + "<style:paragraph-properties style:tab-stop-distance=\"1.25cm\" />"
	    + "<style:text-properties fo:font-size=\"12.0pt\" style:font-size-asian=\"12.0pt\" style:font-name=\"Times New Roman\" fo:language=\"en\" fo:country=\"US\""
	  	+	" style:font-name-asian=\"宋体\" style:language-asian=\"zh\" style:country-asian=\"CN\" style:font-name-complex=\"Tahoma\" style:font-size-complex=\"12pt\"/>"
	  	+"</style:default-style>";

	public static final String _de_table_cell =
		"<style:style style:name=\"de_Default\" style:family=\"table-cell\">"
		+ "<style:text-properties style:font-name=\"Times New Roman\" style:font-name-asian=\"宋体\" style:font-name-complex=\"Tahoma\""
		+ " fo:font-size=\"12.0pt\" style:font-size-asian=\"12.0pt\" style:font-size-complex=\"12.0pt\"/>"
	   +"</style:style>";

	public static final String _table_row =
		"<style:default-style style:family=\"table-row\">"
		+ "<style:table-row-properties fo:keep-together=\"auto\" />"
	  + "</style:default-style>";

	public static final String _standard =
		"<style:style style:name=\"Standard\" style:family=\"paragraph\" style:class=\"text\" />";

	public static final String _text_20_body =
		"<style:style style:name=\"Text_20_body\" style:display-name=\"Text body\""
		+ " style:family=\"paragraph\" style:parent-style-name=\"Standard\" style:class=\"text\">"
		+ "<style:paragraph-properties fo:margin-top=\"0pt\" fo:margin-bottom=\"6.01pt\" />"
	  + "</style:style>";

	public static final String _list =
		"<style:style style:name=\"List\" style:family=\"paragraph\" style:parent-style-name=\"Text_20_body\" style:class=\"list\">"
		+ "<style:text-properties style:font-name-complex=\"Tahoma1\" />"
	  + "</style:style>";

	public static final String _caption =
		"<style:style style:name=\"Caption\" style:family=\"paragraph\" style:parent-style-name=\"Standard\" style:class=\"extra\">"
		+ "<style:paragraph-properties fo:margin-top=\"6.01pt\" fo:margin-bottom=\"6.01pt\" text:number-lines=\"false\" text:line-number=\"0\" />"
		+ "<style:text-properties fo:font-size=\"10pt\" fo:font-style=\"italic\" style:font-size-asian=\"10pt\" style:font-style-asian=\"italic\" style:font-name-complex=\"Tahoma1\" style:font-size-complex=\"10pt\" style:font-style-complex=\"italic\" />"
	  + "</style:style>";

	public static final String _index =
		"<style:style style:name=\"Index\" style:family=\"paragraph\" style:parent-style-name=\"Standard\" style:class=\"index\">"
		+ "<style:paragraph-properties text:number-lines=\"false\" text:line-number=\"0\" />"
		+ "<style:text-properties style:font-name-complex=\"Tahoma1\" />"
	  + "</style:style>";

	public static final String _outline_style =
		"<text:outline-style>"
		+ "<text:outline-level-style text:level=\"1\" style:num-format=\"\">"
			+ "<style:list-level-properties text:min-label-distance=\"10.8pt\" />"
		+ "</text:outline-level-style>"
		+ "<text:outline-level-style text:level=\"2\" style:num-format=\"\">"
	  		+ "<style:list-level-properties text:min-label-distance=\"10.8pt\" />"
	  	+ "</text:outline-level-style>"
	  	+ "<text:outline-level-style text:level=\"3\" style:num-format=\"\">"
	  		+ "<style:list-level-properties text:min-label-distance=\"10.8pt\" />"
	    + "</text:outline-level-style>"
	    + "<text:outline-level-style text:level=\"4\" style:num-format=\"\">"
	    	+ "<style:list-level-properties text:min-label-distance=\"10.8pt\" />"
	    + "</text:outline-level-style>"
	    + "<text:outline-level-style text:level=\"5\" style:num-format=\"\">"
	    	+ "<style:list-level-properties text:min-label-distance=\"10.8pt\" />"
	    + "</text:outline-level-style>"
	    + "<text:outline-level-style text:level=\"6\" style:num-format=\"\">"
	    	+ "<style:list-level-properties text:min-label-distance=\"10.8pt\" />"
	    + "</text:outline-level-style>"
	    + "<text:outline-level-style text:level=\"7\" style:num-format=\"\">"
	    	+ "<style:list-level-properties text:min-label-distance=\"10.8pt\" />"
	    + "</text:outline-level-style>"
	    + "<text:outline-level-style text:level=\"8\" style:num-format=\"\">"
	    	+ "<style:list-level-properties text:min-label-distance=\"10.8pt\" />"
	    + "</text:outline-level-style>"
	    + "<text:outline-level-style text:level=\"9\" style:num-format=\"\">"
	    	+ "<style:list-level-properties text:min-label-distance=\"10.8pt\" />"
	    + "</text:outline-level-style>"
	    + "<text:outline-level-style text:level=\"10\" style:num-format=\"\">"
	    	+ "<style:list-level-properties text:min-label-distance=\"10.8pt\" />"
	    + "</text:outline-level-style>"
	  + "</text:outline-style>";

	public static final String _note_config =
		"<text:notes-configuration text:note-class=\"footnote\" style:num-format=\"1\" text:start-value=\"0\" text:footnotes-position=\"page\" text:start-numbering-at=\"document\" />"
	  + "<text:notes-configuration text:note-class=\"endnote\" style:num-format=\"i\" text:start-value=\"0\" />"
	  + "<text:linenumbering-configuration text:number-lines=\"false\" text:offset=\"14.14pt\" style:num-format=\"1\" text:number-position=\"left\" text:increment=\"5\" />";

	public static final String _content_20_heading =
		"<style:style style:name=\"Contents_20_Heading\" style:display-name=\"Contents Heading\" style:family=\"paragraph\" style:parent-style-name=\"Heading\" style:class=\"index\">"
			+ "<style:paragraph-properties fo:margin-left=\"0pt\" fo:margin-right=\"0pt\" fo:text-indent=\"0pt\" style:auto-text-indent=\"false\" text:number-lines=\"false\" text:line-number=\"0\"/>"
			+ "<style:text-properties fo:font-size=\"16pt\" fo:font-weight=\"bold\" style:font-size-asian=\"16pt\" style:font-weight-asian=\"bold\" style:font-size-complex=\"16pt\" style:font-weight-complex=\"bold\"/>"
		+ "</style:style>";

	public static final String _content_20_1 =
		"<style:style style:name=\"Contents_20_1\" style:display-name=\"Contents 1\" style:family=\"paragraph\" style:parent-style-name=\"Index\" style:class=\"index\">"
		  + "<style:paragraph-properties fo:margin-left=\"0pt\" fo:margin-right=\"0pt\" fo:text-indent=\"0pt\" style:auto-text-indent=\"false\">"
		  + "<style:tab-stops>"
			+ "<style:tab-stop style:position=\"481.86pt\" style:type=\"right\" style:leader-style=\"dotted\" style:leader-text=\".\"/>"
		  + "</style:tab-stops>"
	      + "</style:paragraph-properties>"
        + "</style:style>";

	public static final String _content_20_2 =
		"<style:style style:name=\"Contents_20_2\" style:display-name=\"Contents 2\" style:family=\"paragraph\" style:parent-style-name=\"Index\" style:class=\"index\">"
			+ "<style:paragraph-properties fo:margin-left=\"14.14pt\" fo:margin-right=\"0pt\" fo:text-indent=\"0pt\" style:auto-text-indent=\"false\">"
			+ "<style:tab-stops>"
				+ "<style:tab-stop style:position=\"467.69pt\" style:type=\"right\" style:leader-style=\"dotted\" style:leader-text=\".\"/>"
		    + "</style:tab-stops>"
		    + "</style:paragraph-properties>"
        + "</style:style>";

	public static final String _table_of_content_source =
		"<text:table-of-content-source text:outline-level=\"10\">"
		  + "<text:index-title-template text:style-name=\"Contents_20_Heading\">内容目录</text:index-title-template>"
		  	+ "<text:table-of-content-entry-template text:outline-level=\"1\" text:style-name=\"Contents_20_1\">"
		  	+ "<text:index-entry-chapter />"
		  	+ "<text:index-entry-text />"
		  	+ "<text:index-entry-tab-stop style:type=\"right\" style:leader-char=\".\" />"
		  	+ "<text:index-entry-page-number />"
		  + "</text:table-of-content-entry-template>"
		  +	"<text:table-of-content-entry-template text:outline-level=\"2\" text:style-name=\"Contents_20_2\">"
		  	+ "<text:index-entry-chapter />"
		  	+ "<text:index-entry-text />"
		  	+ "<text:index-entry-tab-stop style:type=\"right\" style:leader-char=\".\" />"
		  	+ "<text:index-entry-page-number />"
		  + "</text:table-of-content-entry-template>"
	  + "</text:table-of-content-source>";


	public static String get_table_of_content_source(){
		//add the styles used by this content source to styles.xml
		//Stored_Data.addStylesInStylesXml(styles_for_toc());

		return _table_of_content_source;
	}

	//private static String styles_for_toc(){
	//	return _standard + _index + _content_20_heading + _content_20_1 + _content_20_2;
	//}
}
