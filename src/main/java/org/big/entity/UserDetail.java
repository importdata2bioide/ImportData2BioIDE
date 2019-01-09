package org.big.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 *<p><b>UserDetail的Entity类</b></p>
 *<p> 此类用于操作SpringSecurity的存储访问用户的重写</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/5 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
public class UserDetail extends User implements UserDetails {

	private static final long serialVersionUID = 7349031122214065665L;

	public UserDetail(User user){
        super(user);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.getRole() == null || this.getRole().length() <1){
            return AuthorityUtils.commaSeparatedStringToAuthorityList("");
        }
        else{
            return AuthorityUtils.commaSeparatedStringToAuthorityList(this.getRole());
        }
//        if(teams == null || teams.size() <1){
//            return AuthorityUtils.commaSeparatedStringToAuthorityList("");
//        }
//        StringBuilder commaBuilder = new StringBuilder();
//        for(Team team : teams){
//            commaBuilder.append(team.getName()).append(",");
//        }
//        String authorities = commaBuilder.substring(0,commaBuilder.length()-1);
//        return AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
    }

    @Override
    public String getUsername() {
        return super.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getNickname() {
        return super.getNickname();
    }
}
