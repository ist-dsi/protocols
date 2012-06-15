<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<h2><bean:message key="label.protocols.create" bundle="PROTOCOLS_RESOURCES"/></h2>

<p class="breadcumbs">
	<span><bean:message key="label.protocol.create.step1" bundle="PROTOCOLS_RESOURCES"/></span> > 
	<span><strong><bean:message key="label.protocol.create.step2" bundle="PROTOCOLS_RESOURCES"/></strong></span> > 
	<span><bean:message key="label.protocol.create.step3" bundle="PROTOCOLS_RESOURCES"/></span> >
	<span><bean:message key="label.protocol.create.step4" bundle="PROTOCOLS_RESOURCES"/></span>
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
	<h3><bean:message key="label.protocols.internalUnits" bundle="PROTOCOLS_RESOURCES"/></h3>
</div>



<br />




<p>

<logic:present name="protocolBean" property="internalResponsibles">

<logic:iterate id="responsible" name="protocolBean" property="internalResponsibles">

<fr:form action="/protocols.do?method=updateBean">

<fr:edit id="protocolBean" name="protocolBean" visible="false" />

<bean:define id="unitOID" type="java.lang.Long" name="responsible" property="unit.OID"/>

<html:hidden bundle="HTMLALT_RESOURCES" name="protocolsForm" property="unitOID" value="<%= unitOID.toString() %>"/>

<table width="100%" class="tstyle3">
<tr>
<th colspan="2">
<bean:write name="responsible" property="unit.presentationName"/>

</th>
</tr>
<tr>

<td width="50%" valign="top" align="center">

<div class="infobox" align="center">
	<p class="dinline"><strong><bean:message key="label.protocols.addResponsible" bundle="PROTOCOLS_RESOURCES"/></strong></p>
</div>

<div align="center">
<fr:edit name="protocolBean">
<fr:schema type="module.protocols.dto.ProtocolCreationBean" bundle="PROTOCOLS_RESOURCES">
	<fr:slot name="newPerson" layout="autoComplete" key="label.person" bundle="ORGANIZATION_RESOURCES">
        <fr:property name="labelField" value="presentationName"/>
		<fr:property name="format" value="${presentationName}"/>
		<fr:property name="minChars" value="2"/>
		<fr:property name="args" value="<%="provider=module.protocols.presentationTier.providers.PeoplePerUnitAutoCompleteProvider,unit=" + unitOID %>"/>
		<fr:property name="size" value="40"/>
	</fr:slot>
</fr:schema>	
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/>
	</fr:layout>
</fr:edit>

<html:submit bundle="PROPERTIES_RESOURCES" property="insertPersonInUnit">
	<bean:message key="button.insertPerson" bundle="PROTOCOLS_RESOURCES" />
</html:submit>

</div>

<br />

<div class="infobox" align="center">
	<p class="dinline"><strong><bean:message key="label.protocols.addPosition" bundle="PROTOCOLS_RESOURCES"/></strong></p>
</div>

<div align="center">
<fr:edit name="protocolBean">
<fr:schema type="module.protocols.dto.ProtocolCreationBean" bundle="PROTOCOLS_RESOURCES">
	<fr:slot name="newPosition" key="label.position" bundle="PROTOCOLS_RESOURCES"/>
</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/>
	</fr:layout>
</fr:edit>

<html:submit bundle="PROPERTIES_RESOURCES" property="insertPosition">
	<bean:message key="button.addPosition" bundle="PROTOCOLS_RESOURCES" />
</html:submit>

</div>


</td>

<td width="50%" valign="top">

<div align="center">

<fr:view name="responsible" property="responsibles">
	<fr:schema type="module.organization.domain.Person" bundle="PROTOCOLS_RESOURCES">
		<fr:slot name="presentationName" key="label.protocols.responsiblesInUnit" bundle="PROTOCOLS_RESOURCES"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle3 mvert05 "/>
        <fr:property name="columnClasses" value="width10em,,tderror1"/>
	</fr:layout>
</fr:view>

<br />
<br />

<logic:equal name="responsible" property="positions.empty" value="false">

<table>
	<tr>
		<th>
			<bean:message key="label.protocols.positions" bundle="PROTOCOLS_RESOURCES"/>
		</th>
	</tr>
	<logic:iterate id="value" name="responsible" property="positions.unmodifiableList">
		<tr>
			<td>
				<bean:write name="value" bundle="PROTOCOLS_RESOURCES"/>
			</td>
		</tr>
	</logic:iterate>
</table>

<html:submit bundle="PROPERTIES_RESOURCES" property="removePositions">
	<bean:message key="button.removePositions" bundle="PROTOCOLS_RESOURCES" />
</html:submit>

</logic:equal>

<br />
<br />

<html:submit bundle="PROPERTIES_RESOURCES" property="removeUnit">
	<bean:message key="button.removeUnit" bundle="PROTOCOLS_RESOURCES" />
</html:submit>

</div>

</td>


</tr>

</table>
<br />

</fr:form>
</logic:iterate>



</logic:present>

<hr />

<fr:form action="/protocols.do?method=updateBean">

<div class="infobox" align="center">
	<p class="dinline"><strong><bean:message key="label.protocols.select.new.unit" bundle="PROTOCOLS_RESOURCES"/> </strong></p>
</div>

<div align="center">

<fr:edit name="protocolBean">
<fr:schema type="module.protocols.dto.ProtocolCreationBean" bundle="PROTOCOLS_RESOURCES">
	<fr:slot name="newUnit" layout="autoComplete" key="label.unit" bundle="ORGANIZATION_RESOURCES">
        <fr:property name="labelField" value="presentationName"/>
		<fr:property name="format" value="${presentationName}"/>
		<fr:property name="minChars" value="2"/>
		<fr:property name="args" value="provider=module.protocols.presentationTier.providers.UnitPerModelAutoCompleteProvider,model=internal"/>
		<fr:property name="size" value="40"/>
	</fr:slot>
</fr:schema>	
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/>
	</fr:layout>
</fr:edit>

<html:submit bundle="PROPERTIES_RESOURCES" property="insertInternalUnit">
	<bean:message key="button.insertUnit" bundle="PROTOCOLS_RESOURCES" />
</html:submit>

</div>

</fr:form>



<fr:form action="/protocols.do?method=prepareCreateExternalResponsibles">

<fr:edit id="protocolBean" name="protocolBean" visible="false" />


<p>
	<html:submit bundle="PROTOCOLS_RESOURCES" altKey="submit.back" property="back">
		<bean:message key="submit.back" bundle="PROTOCOLS_RESOURCES" />
	</html:submit>
	<html:submit bundle="PROTOCOLS_RESOURCES" altKey="submit.submit">
		<bean:message key="submit.next" bundle="PROTOCOLS_RESOURCES" />
	</html:submit>
</p>

</fr:form>