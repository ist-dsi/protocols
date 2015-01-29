<%@ page language="java"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>
<h2><bean:message key="title.protocols.renew" bundle="PROTOCOLS_RESOURCES"/></h2>
<span class="error0 mtop0">
	<html:messages id="message" message="true" bundle="PROTOCOLS_RESOURCES">
		<bean:write name="message" />
	</html:messages>
</span>

<fr:form action="/protocolAlerts.do?method=renewProtocol">

<fr:view name="protocol" property="protocolHistories" schema="show.protocolHistories.toList">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 tdcenter" />
	</fr:layout>
</fr:view> 

<table class="tstyle5">
	<tr>
		<td>
			<bean:message key="label.renewDuring" bundle="PROTOCOLS_RESOURCES"/>:
		</td>
		<td>
			<fr:edit name="protocol" id="protocolHistoryFactory" schema="renew.protocolHistoryFactory">
				<fr:layout name="flow">
					<fr:property name="labelExcluded" value="true"/>
					<fr:property name="validatorClasses" value="error0"/>
				</fr:layout>
			</fr:edit>
		</td>
	</tr>
</table>

<p>
	<html:submit bundle="PROTOCOLS_RESOURCES" altKey="submit.submit">
		<bean:message key="link.protocols.renew" bundle="PROTOCOLS_RESOURCES"/>
	</html:submit>
</p>
</fr:form>
