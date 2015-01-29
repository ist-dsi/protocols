package module.protocols.domain.util;

import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

public enum ProtocolActionType implements IPresentableEnum {
    STUDENTS_INTERCHANGE, TEACHERS_INTERCHANGE, INVESTIGATION_AND_DEVELOPMENT, RENDERING_SERVICES, RENDERING_SERVICE_IST_TEACHER,
    RENDERING_SERVICE_OTHER_INSTITUTION_TEACHER, WORKSHOPS, FORMATION_TRAINEE, DOCUMENTATION, TECHNICAL_COOPERATION,
    POST_GRADUATION, DOUBLE_DEGREE;

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString("resources/ProtocolsResources", "label.protocolActionType." + name());
    }
}
