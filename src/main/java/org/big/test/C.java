package org.big.test;

import java.util.Locale;

import org.big.common.CommUtils;
import org.big.service.ToolServiceImpl;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.internal.util.StringHelper;

public class C {
	
	public static void main(String[] args) {
		ToolServiceImpl t = new ToolServiceImpl();
		String line = "Ep(Tatretus burgeri (Girard, 1855)";
		int caseIndex = t.getSecondUpperCaseIndex(line);
		System.out.println(line.substring(caseIndex-1,caseIndex));
		}
	
	public int getYearStart(String line) {
		int start = -1;
		for (int i = 0; i < line.length() - 4; i++) {
			String tmp = line.substring(i, i + 4);
			if (CommUtils.isNumeric(tmp)) {
				start = i;
				break;
			}
		}
		return start;

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
