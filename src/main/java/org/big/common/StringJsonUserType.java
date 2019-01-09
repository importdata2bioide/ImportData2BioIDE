package org.big.common;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import com.alibaba.fastjson.JSON;

/**
 *<p><b>自定义一个Type用于java和数据库之间的mapping</b></p>
 *<p> 用于SpringDataJPA的拓展</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 21:26</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
public class StringJsonUserType extends JSON implements UserType {

	@Override
	public int[] sqlTypes() {
		return new int[] { Types.JAVA_OBJECT };
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class returnedClass() {
		return String.class;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {

		if (x == null) {

			return y == null;
		}

		return x.equals(y);
	}

	@Override
	public int hashCode(Object x) throws HibernateException {

		return x.hashCode();
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
			throws HibernateException, SQLException {
		if (rs.getString(names[0]) == null) {
			return null;
		}
		return rs.getString(names[0]);
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
			throws HibernateException, SQLException {
		if (value == null) {
			st.setNull(index, Types.OTHER);
			return;
		}

		st.setObject(index, value, Types.OTHER);
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {

		return value;
	}

	@Override
	public boolean isMutable() {
		return true;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		return (String) this.deepCopy(value);
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return this.deepCopy(cached);
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}
}
