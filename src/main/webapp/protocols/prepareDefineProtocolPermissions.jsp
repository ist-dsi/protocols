<%@ page language="java"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<h2><bean:message key="label.protocols.create" bundle="PROTOCOLS_RESOURCES"/></h2>

<p class="breadcumbs">
	<span><bean:message key="label.protocol.create.step1" bundle="PROTOCOLS_RESOURCES"/></span> > 
	<span><bean:message key="label.protocol.create.step2" bundle="PROTOCOLS_RESOURCES"/></span> > 
	<span><bean:message key="label.protocol.create.step3" bundle="PROTOCOLS_RESOURCES"/></span> >
	<span><strong><bean:message key="label.protocol.create.step4" bundle="PROTOCOLS_RESOURCES"/></strong></span>
</p>
<p></p>

<p>
	<span class="error0">
		<html:errors bundle="PROTOCOLS_RESOURCES"/>
		<html:messages id="message" name="errorMessage" message="true" bundle="PROTOCOLS_RESOURCES">
			<bean:write name="message" />
		</html:messages>
	</span>
</p>


<br />

<div align="center">
	<h3><bean:message key="label.protocol.create.step4" bundle="PROTOCOLS_RESOURCES"/></h3>
</div>



<br />

<table width="100%">
<tr>

<td width="50%" valign="top">

<div class="infobox">
	<p class="dinline"><strong><bean:message key="label.protocols.writers" bundle="PROTOCOLS_RESOURCES"/></strong></p>
</div>

<fr:form action="/protocols.do?method=prepareDefineProtocolPermissions">

<fr:edit name="protocolBean">
<fr:schema type="module.protocols.dto.ProtocolCreationBean" bundle="PROTOCOLS_RESOURCES">
	<fr:slot name="writers" layout="menu-select-postback" key="label.user.groups" bundle="MYORG_RESOURCES">
			<fr:property name="providerClass" value="module.protocols.presentationTier.providers.UserGroupsProvider"/>
			<fr:property name="format" value="\${writerGroup.presentationName}" />
	</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tview1"/>
	</fr:layout>
</fr:edit>

</fr:form>

</td>

<td width="50%" valign="top">

<fr:form action="/protocols.do?method=createProtocol">

<logic:present name="protocolBean" property="writers">

<div class="infobox">
	<p class="dinline"><strong><bean:message key="label.protocols.readers" bundle="PROTOCOLS_RESOURCES"/></strong></p>
</div>

<fr:edit name="protocolBean">
<fr:schema type="module.protocols.dto.ProtocolCreationBean" bundle="PROTOCOLS_RESOURCES">
	<fr:slot name="readers" key="label.user.groups" layout="option-select" bundle="MYORG_RESOURCES">
		<fr:property name="providerClass" value="module.protocols.presentationTier.providers.AuthorizationGroupReadersProvider" />
		<fr:property name="eachSchema" value="show.persistentGroup.name"/>
		<fr:property name="eachLayout" value="values"/>
		<fr:property name="classes" value="no-bullets"/>
	</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1"/>
	</fr:layout>
</fr:edit>



</logic:present>

</td>
</tr>
</table>

<p>

<div align="center">

<div class="infobox">
	<p class="dinline"><strong><bean:message key="label.protocols.visibility" bundle="PROTOCOLS_RESOURCES"/></strong></p>
</div>

<fr:edit name="protocolBean">
<fr:schema type="module.protocols.dto.ProtocolCreationBean" bundle="PROTOCOLS_RESOURCES">
	<fr:slot name="visibilityType" key="label.protocols.visibility" layout="radio" bundle="PROTOCOLS_RESOURCES">
		<fr:property name="providerClass" value="module.protocols.presentationTier.providers.ProtocolVisibilityTypeProvider" />
		<fr:property name="eachLayout" value="this-does-not-exist"/>
		<fr:property name="classes" value="no-bullets"/>
	</fr:slot>
</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle3,tview1"/>
	</fr:layout>
</fr:edit>

</div>

<hr />


<fr:edit id="protocolBean" name="protocolBean" visible="false" />

<p>
	<html:submit bundle="PROTOCOLS_RESOURCES" altKey="submit.back" property="back">
		<bean:message key="submit.back" bundle="PROTOCOLS_RESOURCES" />
	</html:submit>
	<html:submit bundle="PROTOCOLS_RESOURCES" altKey="submit.submit">
		<bean:message key="label.protocols.create" bundle="PROTOCOLS_RESOURCES" />
	</html:submit>
</p>

</fr:form>
