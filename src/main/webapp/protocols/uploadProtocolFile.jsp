<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<p class="mtop2 mbottom0" align="center">
<h2>
<bean:message key="link.protocols.uploadFile" bundle="PROTOCOLS_RESOURCES" />
</h2>

<logic:present name="file">


<fr:edit name="file" id="uploadFile" action="/protocols.do?method=uploadProtocolFile">
	<fr:schema type="module.protocols.dto.ProtocolFileUploadBean" bundle="PROTOCOLS_RESOURCES">
		<fr:slot name="inputStream" key="label.file">
			<fr:property name="fileNameSlot" value="filename" />
			<fr:property name="size" value="50"/>
		</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
</fr:edit>

</logic:present>
