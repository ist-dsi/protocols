<%@ page language="java"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<style type="text/css">
.width70 {
	width: 70px;
}
</style>

<h2><bean:message key="label.protocols.search" bundle="PROTOCOLS_RESOURCES" /></h2>

<p></p>
<logic:present name="protocolSearch">

<div id="accordion">

<h3><a href="#"><bean:message key="label.protocols.searchOptions" bundle="PROTOCOLS_RESOURCES"/></a></h3>
<div>
	

	<html:errors/>
	<fr:form action="/protocols.do?method=searchProtocols">

		<div class="mvert15">
			<p class="mvert0"><span class="error0"><fr:message for="protocolBeginDate"/></span></p>
			<p class="mvert0"><span class="error0"><fr:message for="protocolEndDate"/></span></p>
			<p class="mvert0"><span class="error0"><fr:message for="signedDate"/></span></p>
		</div>

		<fr:edit name="protocolSearch" id="number" schema="edit.protocolSearch.number">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle mbottom05"/>
				<fr:property name="columnClasses" value="width100px,width500px,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>

		<logic:equal name="protocolSearch" property="nationalityType" 
				value="<%= module.protocols.dto.ProtocolSearchBean.SearchNationalityType.COUNTRY.toString()%>">
			<fr:edit name="protocolSearch">
				<fr:schema type="module.protocols.dto.ProtocolSearchBean" bundle="PROTOCOLS_RESOURCES">
					<fr:slot name="country" key="label.protocols.country" bundle="PROTOCOLS_RESOURCES" layout="menu-select">
						<fr:property name="providerClass" value="module.geography.presentationTier.provider.CountryProvider"/>
						<fr:property name="format" value="${name.content}" />
					</fr:slot>
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thright thmiddle mbottom05"/>
					<fr:property name="columnClasses" value="width100px,width500px,tdclear tderror1"/>
				</fr:layout>
			</fr:edit>
		</logic:equal>

		<fr:edit name="protocolSearch" id="protocolSearch" schema="edit.protocolSearch.actionType">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05"/>
				<fr:property name="columnClasses" value="width100px,width500px,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>

		<table class="tstyle5 aright mvert05">
		<tr>
			<td>
				<fr:edit name="protocolSearch" id="protocolDates" layout="flow">
					<fr:schema bundle="PROTOCOLS_RESOURCES" type="module.protocols.dto.ProtocolSearchBean">
						<fr:slot name="beginDate" key="label.protocol.active.between" bundle="PROTOCOLS_RESOURCES" layout="picker">
							<fr:property name="formatText" value=""/>
						</fr:slot>
						<fr:slot name="endDate" key="label.protocol.and" bundle="PROTOCOLS_RESOURCES" layout="picker"/>
					</fr:schema>
					<fr:layout name="flow">
						<fr:property name="classes" value=""/>
						<fr:property name="labelTerminator" value=""/>
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
		</table>

		<p class="mbottom15">
		<html:submit bundle="PROTOCOLS_RESOURCES" altKey="submit.submit" property="search">
			<bean:message key="button.search" bundle="PROTOCOLS_RESOURCES"/>
		</html:submit>
		</p>
	</fr:form>

</div>
</div>



<logic:present name="searchResults">

<p>
<em>
<bean:size id="protocolNumber" name="searchResults"/>
<bean:message key="message.protocol.number" arg0="<%= protocolNumber.toString()%>" bundle="PROTOCOLS_RESOURCES"/>
</em>
</p>

	<fr:view name="searchResults" schema="show.protocol.toList" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="columnClasses" value="acenter,width70,width70,acenter,,,aleft"/>
			<fr:property name="link(show)" value="/protocols.do?method=viewProtocolDetails" />
			<fr:property name="key(show)" value="link.protocols.viewDetails" />
			<fr:property name="param(show)" value="OID" />
			<fr:property name="bundle(show)" value="PROTOCOLS_RESOURCES" />
		</fr:layout>
	</fr:view>

</logic:present>

<script>
	$(function() {
		$( "#accordion" ).accordion({
			<logic:present name="searchResults">
				<logic:notEmpty name="searchResults">
					active: false,
					collapsible: true
				</logic:notEmpty>
			</logic:present>
		});
	});
</script>

</logic:present>

