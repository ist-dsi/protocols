<%@ page language="java"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
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

<script type="text/javascript">

function removeUnitFunction(formId) {
	if(confirm("Tem a certeza que pretende remover a unidade?")) {
		document.forms[formId].innerHTML = document.forms[formId].innerHTML + "<input type=\"hidden\" name=\"removeUnit\" value='removeUnit'>";
		document.forms[formId].submit();
	}
}

function removePositionFunction(formId, position) {
	if(confirm("Tem a certeza que pretende remover o cargo?")) {
		document.forms[formId].innerHTML = document.forms[formId].innerHTML + "<input type=\"hidden\" name=\"removePosition\" value='removePosition'>"+
		"<input type=\"hidden\" name=\"position\" value='" + position + "'>";
		document.forms[formId].submit();
	}
}

function removePersonFunction(formId, personId) {
	if(confirm("Tem a certeza que pretende remover a pessoa seleccionada?")) {
		document.forms[formId].innerHTML = document.forms[formId].innerHTML + "<input type=\"hidden\" name=\"removePersonInUnit\" value='removePersonInUnit'>" +
		"<input type=\"hidden\" name=\"personOID\" value='" + personId + "'>";
		document.forms[formId].submit();
	}
}

</script>


<p>

<logic:present name="protocolBean" property="internalResponsibles">

<logic:iterate id="responsible" name="protocolBean" property="internalResponsibles">

<bean:define id="unitOID" type="java.lang.Long" name="responsible" property="unit.OID"/>

<fr:form action="/protocols.do?method=updateBean" id="<%= unitOID.toString() %>">

<fr:edit id="protocolBean" name="protocolBean" visible="false" />

<html:hidden bundle="HTMLALT_RESOURCES" name="protocolsForm" property="unitOID" value="<%= unitOID.toString() %>"/>

<table width="100%" class="tstyle3" style="position:relative;">
<tr>
<th colspan="2">
<bean:write name="responsible" property="unit.presentationName"/>

<img src="images/delet.png" style="position:absolute; right: 5px; top: 8px" onClick="<%= "removeUnitFunction('" + unitOID.toString() + "');"%>"/>
</th>
</tr>
<tr>

<td width="50%" valign="top" align="center">

<div class="infobox" align="center">
	<p class="dinline"><strong><bean:message key="label.protocols.addResponsible" bundle="PROTOCOLS_RESOURCES"/></strong></p>
</div>

<div align="left">
<fr:edit name="protocolBean">
<fr:schema type="module.protocols.dto.ProtocolCreationBean" bundle="PROTOCOLS_RESOURCES">
	<fr:slot name="newPerson" layout="autoComplete" key="label.person" bundle="ORGANIZATION_RESOURCES">
        <fr:property name="labelField" value="presentationName"/>
		<fr:property name="format" value="\${presentationName}"/>
		<fr:property name="minChars" value="2"/>
		<fr:property name="args" value="provider=module.protocols.presentationTier.providers.PeoplePerUnitAutoCompleteProvider,unit=${unitOID}"/>
		<fr:property name="size" value="50"/>
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

<div align="center">

<logic:notEmpty name="responsible" property="responsibles">

<table width="100%">
	<tr>
		<th colspan="2">
			<bean:message key="label.protocols.responsiblesInUnit" bundle="PROTOCOLS_RESOURCES"/>
		</th>
	</tr>
	<logic:iterate id="value" name="responsible" property="responsibles">
	
		<bean:define id="oid" name="value" property="OID" />
	
		<tr>
			<td style="text-align: left">
				<bean:write name="value" property="presentationName" bundle="PROTOCOLS_RESOURCES"/> 
			</td>
			<td width="16px">
				<img src="images/delet.png" style="margin-top: 3px" onClick="<%= "removePersonFunction('" + unitOID.toString() + "', '" + oid  +"');"%>"/>
			</td>
		</tr>
	</logic:iterate>
</table>

</logic:notEmpty>

</div>


</td>

<td width="50%" valign="top">

<div class="infobox" align="center">
	<p class="dinline"><strong><bean:message key="label.protocols.addPosition" bundle="PROTOCOLS_RESOURCES"/></strong></p>
</div>

<div align="left">
<fr:edit name="protocolBean">
<fr:schema type="module.protocols.dto.ProtocolCreationBean" bundle="PROTOCOLS_RESOURCES">
	<fr:slot name="newPosition" key="label.position" bundle="PROTOCOLS_RESOURCES">
		<fr:property name="size" value="50"/>
	</fr:slot>
</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/>
	</fr:layout>
</fr:edit>

<html:submit bundle="PROPERTIES_RESOURCES" property="insertPosition">
	<bean:message key="button.addPosition" bundle="PROTOCOLS_RESOURCES" />
</html:submit>

</div>


<div align="center">

<br />

<logic:equal name="responsible" property="positions.empty" value="false">

<table width="100%">
	<tr>
		<th colspan="2">
			<bean:message key="label.protocols.positions" bundle="PROTOCOLS_RESOURCES"/>
		</th>
	</tr>
	
	<logic:iterate id="value" name="responsible" property="positions.unmodifiableList">
		
		<tr>
			<td style="text-align: left">
				<bean:write name="value" bundle="PROTOCOLS_RESOURCES"/> 
			</td>
			<td width="16px">
				<img src="images/delet.png" style="margin-top: 3px" onClick="<%= "removePositionFunction('" + unitOID.toString() + "', '" + value  +"');"%>"/>
			</td>
		</tr>
	</logic:iterate>
	
</table>

</logic:equal>

<br />
<br />

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
		<fr:property name="format" value="\${presentationName}"/>
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
	<html:submit bundle="PROTOCOLS_RESOURCES" altKey="submit.submit" style="align: right">
		<bean:message key="submit.next" bundle="PROTOCOLS_RESOURCES" />
	</html:submit>
</p>

</fr:form>
