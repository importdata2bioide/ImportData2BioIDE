package org.big.test;

import java.util.List;
import java.util.Locale;

import org.big.common.CommUtils;
import org.big.entity.Taxon;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.internal.util.StringHelper;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;

public class C {
	
	public static void main(String[] args) {
		String rgex = "\\([0-9]*\\)|\\（[0-9]*\\）";  
		String line = "()蒲氏黏盲鳗(斯蒂芬)";
		List<String> subUtil = CommUtils.getSubUtil(line , rgex);
		for (String str : subUtil) {
			line = line.replace(str, "");
		}
		System.out.println(line);
	}
	
	public String apply(String name) {
		if (name == null) {
			return null;
		}
		//大写字母前加下划线
		StringBuilder builder = new StringBuilder(name.replace('.', '_'));
		for (int i = 1; i < builder.length() - 1; i++) {
			if (isUnderscoreRequired(builder.charAt(i - 1), builder.charAt(i),
					builder.charAt(i + 1))) {
				builder.insert(i++, '_');
			}
		}
		//大写变小写
		name = builder.toString().toLowerCase(Locale.ROOT);
		return name;
	}
	
	
	
	protected boolean isCaseInsensitive(JdbcEnvironment jdbcEnvironment) {
		return true;
	}

	private boolean isUnderscoreRequired(char before, char current, char after) {
		return Character.isLowerCase(before) && Character.isUpperCase(current)
				&& Character.isLowerCase(after);
	}

	
	public static Identifier toIdentifier(String text) {
		if ( StringHelper.isEmpty( text ) ) {
			return null;
		}
		final String trimmedText = text.trim();
		if ( isQuoted( trimmedText ) ) {
			final String bareName = trimmedText.substring( 1, trimmedText.length() - 1 );
			return new Identifier( bareName, true );
		}
		else {
			return new Identifier( trimmedText, false );
		}
	}
	
	public static boolean isQuoted(String name) {
		return ( name.startsWith( "`" ) && name.endsWith( "`" ) )
				|| ( name.startsWith( "[" ) && name.endsWith( "]" ) )
				|| ( name.startsWith( "\"" ) && name.endsWith( "\"" ) );
	}



}
