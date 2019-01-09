package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 *<p><b>FlywaySchemaHistory的Entity类</b></p>
 *<p> FlywaySchemaHistory的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name="flyway_schema_history", schema = "biodata")
public class FlywaySchemaHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="installed_rank")
	private int installedRank;

	private int checksum;

	private String description;

	@Column(name="execution_time")
	private int executionTime;

	@Column(name="installed_by")
	private String installedBy;

	@Column(name="installed_on")
	private Timestamp installedOn;

	private String script;

	private byte success;

	private String type;

	private String version;

	public FlywaySchemaHistory() {
	}

	public int getInstalledRank() {
		return this.installedRank;
	}

	public void setInstalledRank(int installedRank) {
		this.installedRank = installedRank;
	}

	public int getChecksum() {
		return this.checksum;
	}

	public void setChecksum(int checksum) {
		this.checksum = checksum;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getExecutionTime() {
		return this.executionTime;
	}

	public void setExecutionTime(int executionTime) {
		this.executionTime = executionTime;
	}

	public String getInstalledBy() {
		return this.installedBy;
	}

	public void setInstalledBy(String installedBy) {
		this.installedBy = installedBy;
	}

	public Timestamp getInstalledOn() {
		return this.installedOn;
	}

	public void setInstalledOn(Timestamp installedOn) {
		this.installedOn = installedOn;
	}

	public String getScript() {
		return this.script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public byte getSuccess() {
		return this.success;
	}

	public void setSuccess(byte success) {
		this.success = success;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + checksum;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + executionTime;
		result = prime * result + ((installedBy == null) ? 0 : installedBy.hashCode());
		result = prime * result + ((installedOn == null) ? 0 : installedOn.hashCode());
		result = prime * result + installedRank;
		result = prime * result + ((script == null) ? 0 : script.hashCode());
		result = prime * result + success;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FlywaySchemaHistory other = (FlywaySchemaHistory) obj;
		if (checksum != other.checksum)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (executionTime != other.executionTime)
			return false;
		if (installedBy == null) {
			if (other.installedBy != null)
				return false;
		} else if (!installedBy.equals(other.installedBy))
			return false;
		if (installedOn == null) {
			if (other.installedOn != null)
				return false;
		} else if (!installedOn.equals(other.installedOn))
			return false;
		if (installedRank != other.installedRank)
			return false;
		if (script == null) {
			if (other.script != null)
				return false;
		} else if (!script.equals(other.script))
			return false;
		if (success != other.success)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

}