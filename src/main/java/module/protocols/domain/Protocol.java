package module.protocols.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import module.geography.domain.Country;
import module.protocols.domain.util.ProtocolAction;
import module.protocols.domain.util.ProtocolResponsibleType;
import module.protocols.dto.ProtocolCreationBean;
import module.protocols.dto.ProtocolCreationBean.ProtocolResponsibleBean;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.StringNormalizer;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import pt.ist.fenixframework.Atomic;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;

/**
 * 
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class Protocol extends Protocol_Base {

    public static enum RenewTime implements IPresentableEnum {
        YEARS, MONTHS;

        @Override
        public String getLocalizedName() {
            return BundleUtil.getString("resources/ProtocolsResources", "label.renewTime." + name());
        }
    }

    public Protocol() {
        super();
        setProtocolManager(ProtocolManager.getInstance());
        super.setProtocolNumber(new LocalDate().getYear() + "-" + ProtocolManager.getInstance().getNewProtocolNumber());
    }

    public void delete() {
        for (ProtocolHistory history : getProtocolHistoriesSet()) {
            history.delete();
        }
        setProtocolManager(null);
        deleteDomainObject();
    }

    public boolean isActive() {
        return getCurrentProtocolHistory() != null;
    }

    public ProtocolHistory getCurrentProtocolHistory() {
        return getProtocolHistoriesSet().stream().filter(ProtocolHistory::isActive).findAny().orElse(null);
    }

    public ProtocolHistory getLastProtocolHistory() {
        return getProtocolHistoriesSet().stream().max(ProtocolHistory.COMPARATOR_BY_END_DATE).orElse(null);
    }

    public ProtocolHistory getPresentableProtocolHistory() {
        ProtocolHistory current = getCurrentProtocolHistory();
        if (current == null) {
            current = getLastProtocolHistory();
        }
        return current;
    }

    public List<ProtocolHistory> getCurrentAndFutureProtocolHistories() {
        return Ordering.from(ProtocolHistory.COMPARATOR_BY_BEGIN_DATE).sortedCopy(
                Iterables.filter(getProtocolHistoriesSet(), new Predicate<ProtocolHistory>() {

                    @Override
                    public boolean apply(ProtocolHistory history) {
                        return !history.isPast();
                    }

                }));

    }

    public boolean hasExternalPartner(final String partnerName) {

        final String name = StringNormalizer.normalize(partnerName);

        return Iterables.any(getProtocolResponsibleSet(), new Predicate<ProtocolResponsible>() {

            @Override
            public boolean apply(ProtocolResponsible responsible) {

                if (responsible.getType() != ProtocolResponsibleType.EXTERNAL) {
                    return false;
                }

                String unitName = StringNormalizer.normalize(responsible.getUnit().getPartyName().getContent());

                return unitName.equals(name);
            }
        });
    }

    @Atomic
    public void renewFor(Integer duration, RenewTime renewTime) {

        LocalDate beginDate = getLastProtocolHistory().getEndDate();
        LocalDate endDate = beginDate;
        if (renewTime == RenewTime.YEARS) {
            endDate = endDate.plusYears(duration);
        } else if (renewTime == RenewTime.MONTHS) {
            endDate = endDate.plusMonths(duration);
        }

        new ProtocolHistory(this, beginDate, endDate);
    }

    @Atomic
    public static Protocol createProtocol(ProtocolCreationBean protocolBean) {

        Protocol protocol = new Protocol();

        protocol.updateFromBean(protocolBean);

        return protocol;
    }

    @Atomic
    public void updateFromBean(ProtocolCreationBean protocolBean) {

        this.setSignedDate(protocolBean.getSignedDate());
        this.setScientificAreas(protocolBean.getScientificAreas());
        this.setProtocolAction(new ProtocolAction(protocolBean.getActionTypes(), protocolBean.getOtherActionTypes()));
        this.setObservations(protocolBean.getObservations());

        if (protocolBean.getRemovedResponsibles() != null) {
            for (ProtocolResponsible responsible : protocolBean.getRemovedResponsibles()) {
                if (responsible != null) {
                    this.removeProtocolResponsible(responsible);
                }
            }
        }

        for (ProtocolResponsibleBean bean : protocolBean.getInternalResponsibles()) {
            ProtocolResponsible responsible = bean.getProtocolResponsible();
            if (this.getProtocolResponsibleSet().contains(responsible)) {
                responsible.updateFromBean(bean);
            } else {
                ProtocolResponsible newResponsible = new ProtocolResponsible(ProtocolResponsibleType.INTERNAL);
                newResponsible.updateFromBean(bean);

                this.addProtocolResponsible(newResponsible);
            }
        }

        for (ProtocolResponsibleBean bean : protocolBean.getExternalResponsibles()) {
            ProtocolResponsible responsible = bean.getProtocolResponsible();
            if (this.getProtocolResponsibleSet().contains(responsible)) {
                responsible.updateFromBean(bean);
            } else {
                ProtocolResponsible newResponsible = new ProtocolResponsible(ProtocolResponsibleType.EXTERNAL);
                newResponsible.updateFromBean(bean);

                this.addProtocolResponsible(newResponsible);
            }
        }

        for (PersistentGroup group : getReaderGroupsSet()) {
            removeReaderGroups(group);
        }

        for (Group group : protocolBean.getReaders()) {
            addReaderGroups(group.toPersistentGroup());
        }

        this.setWriterGroup(protocolBean.getWriters());

        this.setVisibilityType(protocolBean.getVisibilityType());

        ProtocolHistory currentProtocolHistory = getCurrentProtocolHistory();

        if (currentProtocolHistory != null) {
            currentProtocolHistory.setBeginDate(protocolBean.getBeginDate());
            currentProtocolHistory.setEndDate(protocolBean.getEndDate());
        } else {
            new ProtocolHistory(this, protocolBean.getBeginDate(), protocolBean.getEndDate());
        }

    }

    public boolean canBeReadByUser(final User user) {
        return getVisibilityType() != ProtocolVisibilityType.RESTRICTED || belongsToReadersGroup(user)
                || canBeWrittenByUser(user);
    }

    public boolean canFilesBeReadByUser(User user) {
        return getVisibilityType() == ProtocolVisibilityType.TOTAL || belongsToReadersGroup(user) || canBeWrittenByUser(user);
    }

    public boolean canBeWrittenByUser(final User user) {
        final PersistentGroup pg = getWriterGroup().getAuthorizedWriterGroup();
        return (pg != null && pg.isMember(user)) || ProtocolManager.managers().isMember(user);
    }

    private boolean belongsToReadersGroup(User user) {
        return getReaderGroupsSet().stream().filter((group) -> group.isMember(user)).findAny().isPresent();
    }

    public String getPartners() {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (ProtocolResponsible responsible : getProtocolResponsibleSet()) {
            if (responsible.getType() == ProtocolResponsibleType.INTERNAL) {
                continue;
            }

            if (first) {
                first = false;
            } else {
                builder.append(", ");
            }

            builder.append(responsible.getUnit().getPartyName().getContent());
        }
        return builder.toString();
    }

    public String getAllResponsibles() {
        StringBuilder builder = new StringBuilder();
        for (ProtocolResponsible responsible : getProtocolResponsibleSet()) {

            String responsibleStr = responsible.getPresentationString();

            if (builder.length() > 0 && !responsibleStr.isEmpty()) {
                builder.append(", ");
            }

            builder.append(responsible.getPresentationString());

        }
        return builder.toString();
    }

    public String getCountry() {
        StringBuilder builder = new StringBuilder();
        Set<String> str = new HashSet<>();
        for (ProtocolResponsible responsible : getProtocolResponsibleSet()) {
            if (responsible.getType() == ProtocolResponsibleType.INTERNAL) {
                continue;
            }

            String responsibleStr = responsible.getCountryDescription();
            if (!str.contains(responsibleStr)) {
                str.add(responsibleStr);
                if (builder.length() > 0 && !responsibleStr.isEmpty()) {
                    builder.append(", ");
                }

                builder.append(responsibleStr);
            }
        }
        return builder.toString();
    }

    @Atomic
    public void uploadFile(String filename, byte[] contents) {
        new ProtocolFile(this, filename, contents);
    }

    public boolean isNational() {
        for (ProtocolResponsible responsible : getProtocolResponsibleSet()) {
            if (responsible.getType() == ProtocolResponsibleType.EXTERNAL && responsible.getCountry() != null
                    && responsible.getCountry().equals(Country.getPortugal())) {
                return true;
            }
        }
        return false;
    }

    public boolean isInternational() {
        for (ProtocolResponsible responsible : getProtocolResponsibleSet()) {
            if (responsible.getType() == ProtocolResponsibleType.EXTERNAL && responsible.getCountry() != null
                    && !responsible.getCountry().equals(Country.getPortugal())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasNationality() {
        for (ProtocolResponsible responsible : getProtocolResponsibleSet()) {
            if (responsible.getType() == ProtocolResponsibleType.EXTERNAL && responsible.getCountry() != null) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPartnerInCountry(Country country) {

        if (country == null) {
            return true;
        }

        for (ProtocolResponsible responsible : getProtocolResponsibleSet()) {
            if (responsible.getType() == ProtocolResponsibleType.EXTERNAL && country.equals(responsible.getCountry())) {
                return true;
            }
        }
        return false;
    }

    public String getVisibilityDescription() {
        switch (getVisibilityType()) {
        case PROTOCOL:
            return BundleUtil.getString("resources/ProtocolsResources", "label.protocols.visibility.protocol",
                    generateVisibilityString());
        case RESTRICTED:
            return BundleUtil.getString("resources/ProtocolsResources", "label.protocols.visibility.restricted",
                    generateVisibilityString());
        case TOTAL:
            return BundleUtil.getString("resources/ProtocolsResources", "label.protocols.visibility.total");
        default:
            return "";
        }
    }

    private String generateVisibilityString() {
        Set<PersistentGroup> groups = new HashSet<PersistentGroup>();

        groups.add(getWriterGroup().getAuthorizedWriterGroup());

        for (PersistentGroup group : getReaderGroupsSet()) {
            groups.add(group);
        }

        StringBuilder builder = new StringBuilder(groups.size());

        for (PersistentGroup group : groups) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(group.getPresentationName());
        }

        return builder.toString();
    }

    @Override
    public String getProtocolNumber() {
        return super.getProtocolNumber();
    }

    public Stream<Group> getProtocolReaders() {
        return getReaderGroupsSet().stream().map(PersistentGroup::toGroup);
    }
}
