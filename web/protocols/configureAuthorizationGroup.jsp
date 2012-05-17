<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<div align="center">

<br />

<p class="mtop2 mbottom0" align="center">
<h2>
<bean:message key="label.protocolSystem.configureAuthorizationGroup" bundle="PROTOCOLS_RESOURCES" />
</h2>

<p>

<bean:write name="bean" property="group.authorizedWriterGroup.name"/>

<p>

<fr:form action="/protocols.do?method=configureAuthorizationGroup">

<fr:edit name="bean">
<fr:schema type="module.protocols.dto.AuthorizationGroupBean" bundle="PROTOCOLS_RESOURCES">
	<fr:slot name="authorizedGroups" key="label.user.groups" layout="option-select" bundle="MYORG_RESOURCES">
		<fr:property name="providerClass" value="myorg.presentationTier.renderers.providers.AccessibilityGroupsProvider" />
		<fr:property name="eachSchema" value="show.persistentGroup.name"/>
		<fr:property name="eachLayout" value="values"/>
		<fr:property name="classes" value="no-bullets"/>
	</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1"/>
	</fr:layout>
</fr:edit>


<p>
	<html:submit bundle="PROTOCOLS_RESOURCES" altKey="submit.submit">
		<bean:message key="submit.submit" bundle="PROTOCOLS_RESOURCES" />
	</html:submit>
</p>

</fr:form>

</div>
