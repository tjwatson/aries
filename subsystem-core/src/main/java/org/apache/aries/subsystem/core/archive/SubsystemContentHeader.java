/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.aries.subsystem.core.archive;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.aries.subsystem.core.internal.ResourceHelper;
import org.osgi.framework.Version;
import org.osgi.framework.VersionRange;
import org.osgi.resource.Requirement;
import org.osgi.resource.Resource;
import org.osgi.service.subsystem.SubsystemConstants;

public class SubsystemContentHeader extends AbstractClauseBasedHeader<SubsystemContentHeader.Clause> implements RequirementHeader<SubsystemContentHeader.Clause> {
    public static class Clause extends AbstractClause {
		public static final String ATTRIBUTE_VERSION = VersionRangeAttribute.NAME_VERSION;
		public static final String ATTRIBUTE_TYPE = TypeAttribute.NAME;
		public static final String DIRECTIVE_RESOLUTION = ResolutionDirective.NAME;
		public static final String DIRECTIVE_STARTORDER = StartOrderDirective.NAME;
		
		public Clause(String clause) {
			super(
					parsePath(clause, Patterns.SYMBOLIC_NAME, false), 
					parseParameters(clause, true),
					generateDefaultParameters(
							TypeAttribute.DEFAULT,
							VersionRangeAttribute.DEFAULT_VERSION,
							ResolutionDirective.MANDATORY,
							// This is an implementation specific start-order directive
							// value. The specification states there is no default value.
							new StartOrderDirective("0")));
		}
		
		public Clause(Resource resource) {
			this(appendResource(resource, new StringBuilder()).toString());
		}
		
		public boolean contains(Resource resource) {
			return getSymbolicName().equals(
					ResourceHelper.getSymbolicNameAttribute(resource))
					&& getVersionRange().includes(
							ResourceHelper.getVersionAttribute(resource))
					&& getType().equals(
							ResourceHelper.getTypeAttribute(resource));
		}
		
		public String getSymbolicName() {
			return path;
		}
		
		public int getStartOrder() {
			return ((StartOrderDirective)getDirective(DIRECTIVE_STARTORDER)).getStartOrder();
		}
		
		public String getType() {
			return ((TypeAttribute)getAttribute(ATTRIBUTE_TYPE)).getType();
		}
		
		public VersionRange getVersionRange() {
			return ((VersionRangeAttribute)getAttribute(ATTRIBUTE_VERSION)).getVersionRange();
		}
		
		public boolean isMandatory() {
			return ((ResolutionDirective)getDirective(DIRECTIVE_RESOLUTION)).isMandatory();
		}
		
		public SubsystemContentRequirement toRequirement(Resource resource) {
			return new SubsystemContentRequirement(this, resource);
		}
	}
	
	public static final String NAME = SubsystemConstants.SUBSYSTEM_CONTENT;
	
	public static SubsystemContentHeader newInstance(Collection<Resource> resources) {
		StringBuilder builder = new StringBuilder();
		for (Resource resource : resources) {
			appendResource(resource, builder);
			builder.append(',');
		}
		// Remove the trailing comma.
		// TODO Intentionally letting the exception propagate since there must be at least one resource.
		builder.deleteCharAt(builder.length() - 1);
		return new SubsystemContentHeader(builder.toString());
	}
	
	private static StringBuilder appendResource(Resource resource, StringBuilder builder) {
		String symbolicName = ResourceHelper.getSymbolicNameAttribute(resource);
		Version version = ResourceHelper.getVersionAttribute(resource);
		String type = ResourceHelper.getTypeAttribute(resource);
		builder.append(symbolicName)
			.append(';')
			.append(Clause.ATTRIBUTE_VERSION)
			.append('=')
			.append(version.toString())
			.append(';')
			.append(Clause.ATTRIBUTE_TYPE)
			.append('=')
			.append(type);
		return builder;
	}
	
	public SubsystemContentHeader(Collection<Clause> clauses) {
		super(clauses);
	}
	
	public SubsystemContentHeader(String value) {
		super(
				value, 
				new ClauseFactory<Clause>() {
					@Override
					public Clause newInstance(String clause) {
						return new Clause(clause);
					}
				});
	}
	
	public boolean contains(Resource resource) {
		return getClause(resource) != null;
	}
	
	public Clause getClause(Resource resource) {
		String symbolicName = ResourceHelper.getSymbolicNameAttribute(resource);
		Version version = ResourceHelper.getVersionAttribute(resource);
		String type = ResourceHelper.getTypeAttribute(resource);
		for (Clause clause : clauses) {
			if (symbolicName.equals(clause.getPath())
					&& clause.getVersionRange().includes(version)
					&& type.equals(clause.getType()))
				return clause;
		}
		return null;
	}
	
	public boolean isMandatory(Resource resource) {
		Clause clause = getClause(resource);
		if (clause == null)
			return false;
		return clause.isMandatory();
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getValue() {
		return toString();
	}

	@Override
	public List<Requirement> toRequirements(Resource resource) {
		List<Requirement> requirements = new ArrayList<Requirement>(clauses.size());
		for (Clause clause : clauses)
			requirements.add(clause.toRequirement(resource));
		return requirements;
	}
}
