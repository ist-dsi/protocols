<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<br />
<h2><bean:message key="label.protocols.editDates" bundle="PROTOCOLS_RESOURCES"/></h2>

<p>

<p>
	<span class="error0">
		<html:errors bundle="PROTOCOLS_RESOURCES"/>
		<html:messages id="message" name="errorMessage" message="true" bundle="PROTOCOLS_RESOURCES">
			<bean:write name="message" />
		</html:messages>
	</span>
</p>

<logic:present name="protocolHistory">

<fr:edit name="protocolHistory" action="/protocols.do?method=editProtocolHistory">
<fr:schema type="module.protocols.dto.ProtocolHistoryBean" bundle="PROTOCOLS_RESOURCES">
	<fr:slot name="beginDate" key="label.protocols.beginDate" bundle="PROTOCOLS_RESOURCES" layout="picker">
		<fr:property name="size" value="10"/>
		<fr:property name="maxLength" value="10"/>
		<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.DateValidator">
			<fr:property name="dateFormat" value="dd/MM/yyyy"/>
		</fr:validator>
		<fr:property name="formatText" value=""/>
	</fr:slot>
	<fr:slot name="endDate" key="label.protocols.endDate" bundle="PROTOCOLS_RESOURCES" layout="picker">
		<fr:property name="size" value="10"/>
		<fr:property name="maxLength" value="10"/>
		<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.DateValidator">
			<fr:property name="dateFormat" value="dd/MM/yyyy"/>
		</fr:validator>
		<fr:property name="formatText" value=""/>
	</fr:slot>
</fr:schema>	
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/>
	</fr:layout>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright thmiddle" />
		<fr:property name="columnClasses" value=",,tderror1 tdclear" />
	</fr:layout>
	<fr:destination name="cancel" path="/protocols.do?method=showAlerts"/>
</fr:edit>

</logic:present>
