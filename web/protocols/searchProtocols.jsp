<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

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
				<fr:edit name="protocolSearch" id="protocolBeginDate" schema="edit.protocolSearch.protocolBeginDate" layout="flow">
					<fr:layout name="flow">
						<fr:property name="classes" value=""/>
						<fr:property name="labelTerminator" value=""/>
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
		<tr>
			<td>
				<fr:edit name="protocolSearch" id="protocolEndDate" schema="edit.protocolSearch.protocolEndDate" layout="flow">
					<fr:layout name="flow">
						<fr:property name="classes" value=""/>
						<fr:property name="labelTerminator" value=""/>
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
		<tr>
			<td>
				<fr:edit name="protocolSearch" id="signedDate" schema="edit.protocolSearch.signedDate" layout="flow">
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
			<fr:property name="columnClasses" value="acenter,width165px aleft,acenter,,,aleft"/>
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

