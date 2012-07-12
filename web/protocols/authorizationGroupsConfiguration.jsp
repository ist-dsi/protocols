<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<div align="center">

<p class="mtop2 mbottom0" align="center">
<h2>
<bean:message key="label.protocolSystem.configureAuthorizationGroups" bundle="PROTOCOLS_RESOURCES" />
</h2>

<logic:present name="authorizationGroups">

<fr:view name="authorizationGroups">
	<fr:schema type="module.protocols.domain.ProtocolAuthorizationGroup" bundle="PROTOCOLS_RESOURCES">
		<fr:slot name="authorizedWriterGroup.name" key="label.protocolSystem.authorizationGroups" bundle="PROTOCOLS_RESOURCES"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle3 mvert05 "/>
        <fr:property name="columnClasses" value="width10em,,tderror1"/>
        <fr:property name="link(configure)" value="/protocols.do?method=configureAuthorizationGroup" />
		<fr:property name="key(configure)" value="link.configure" />
		<fr:property name="param(configure)" value="OID" />
		<fr:property name="bundle(configure)" value="PROTOCOLS_RESOURCES" />
       	<fr:property name="link(remove)" value="/protocols.do?method=removeAuthorizationGroup" />
		<fr:property name="key(remove)" value="link.remove" />
		<fr:property name="param(remove)" value="OID" />
		<fr:property name="bundle(remove)" value="MYORG_RESOURCES" />
		<fr:property name="confirmationKey(remove)" value="label.protocols.authorizationGroup.removalWarning" />
		<fr:property name="confirmationBundle(remove)" value="PROTOCOLS_RESOURCES" />
	</fr:layout>
</fr:view>

</logic:present>

<br />

<hr />

<strong><bean:message key="label.protocolSystem.addAuthorizationGroup" bundle="PROTOCOLS_RESOURCES" /></strong>


<fr:form action="/protocols.do?method=createNewAuthorizationGroup">

<fr:edit id="newGroupBean" name="newGroupBean">
<fr:schema type="myorg.util.VariantBean" bundle="PROTOCOLS_RESOURCES">
		<fr:slot name="domainObject" layout="menu-select" key="label.user.groups" bundle="MYORG_RESOURCES">
			<fr:property name="providerClass" value="module.protocols.presentationTier.providers.PersistentGroupsProvider"/>
			<fr:property name="format" value="${name}" />
	</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/>
	</fr:layout>
</fr:edit>

<p>
	<html:submit bundle="PROTOCOLS_RESOURCES" altKey="submit.submit">
		<bean:message key="submit.add" bundle="PROTOCOLS_RESOURCES" />
	</html:submit>
</p>

</fr:form>

</div>