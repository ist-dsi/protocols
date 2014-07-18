package module.protocols.domain;

import org.fenixedu.bennu.core.groups.Group;

public class PersistentProtocolCreatorsGroup extends PersistentProtocolCreatorsGroup_Base {

    public PersistentProtocolCreatorsGroup() {
        super();
    }

    @Override
    public Group toGroup() {
        return ProtocolCreatorsGroup.get();
    }

}
