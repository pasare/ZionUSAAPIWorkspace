package org.zionusa.event.domain.eventTeam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.zionusa.base.enums.EApplicationRole;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.exceptions.NotFoundException;
import org.zionusa.event.dao.EventBranchViewDao;
import org.zionusa.event.dao.EventManagementTeamViewDao;
import org.zionusa.event.domain.eventProposal.EventProposalsDao;
import org.zionusa.event.domain.*;
import org.zionusa.event.domain.eventProposal.EventProposal;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@CacheConfig(cacheNames = "event-teams-cache")
public class EventTeamsService extends BaseService<EventTeam, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(EventTeamsService.class);
    private final EventBranchViewDao eventBranchViewDao;
    private final EventManagementTeamViewDao eventManagementTeamViewDao;
    private final EventProposalsDao eventProposalsDao;

    @Value("${microsoft.tenant.defaultName}")
    private String defaultCategoryName;

    @Autowired
    public EventTeamsService(EventTeamsDao eventTeamsDao,
                             EventBranchViewDao eventBranchViewDao,
                             EventManagementTeamViewDao eventManagementTeamViewDao,
                             EventProposalsDao eventProposalsDao) {
        super(eventTeamsDao, logger, EventTeam.class);
        this.eventManagementTeamViewDao = eventManagementTeamViewDao;
        this.eventBranchViewDao = eventBranchViewDao;
        this.eventProposalsDao = eventProposalsDao;
    }

    public List<EventManagementBranch> getAllEventManagementTeam() {
        // Dao call to get all event teams
        List<EventManagementTeamView> teamMembers = eventManagementTeamViewDao.findAll();
        List<EventManagementTeamView> managementTeam = eventManagementTeamViewDao.
            getEventManagementTeamViewsByApplicationRoleNameContainingOrApplicationRoleNameOrApplicationRoleNameContainingOrApplicationRoleNameContainingOrApplicationRoleNameContainingOrApplicationRoleNameContainingOrApplicationRoleNameContainingOrApplicationRoleNameContainingOrApplicationRoleNameContaining(
                EApplicationRole.EVENT_DEFAULT_ADMIN.getValue(),
                EApplicationRole.EVENT_DEFAULT_GA.getValue(),
                EApplicationRole.EVENT_DEFAULT_MANAGER.getValue(),
                EApplicationRole.EVENT_ASEZ_IUBA_ADMIN.getValue(),
                EApplicationRole.EVENT_ASEZ_IUBA_GA.getValue(),
                EApplicationRole.EVENT_ASEZ_IUBA_MANAGER.getValue(),
                EApplicationRole.EVENT_ASEZ_WAO_IWBA_ADMIN.getValue(),
                EApplicationRole.EVENT_ASEZ_WAO_IWBA_GA.getValue(),
                EApplicationRole.EVENT_ASEZ_WAO_IWBA_MANAGER.getValue()
            );
        List<EventBranchView> branches = eventBranchViewDao.findAll();

        // map members by branch/branch id. map<int, list<EventManagementTeamView>
        Map<Integer, List<EventManagementTeamView>> teamsMap = new HashMap<>();
        teamMembers.forEach(member -> {
            teamsMap.computeIfAbsent(member.getBranchId(), k -> new ArrayList<>());
            teamsMap.get(member.getBranchId()).add(member);
        });
        return branches.stream().map(branch -> {
                List<EventManagementTeamView> viewList = teamsMap.get(branch.getId());
                if (viewList != null) {
                    managementTeam.addAll(viewList);
                }
                return getEventTeamMembers(branch, managementTeam, false);
            }).filter(team -> !team.getTeams().isEmpty())
            .collect(Collectors.toList());
    }

    public EventManagementBranch getEventTeamMembers(EventBranchView branch, List<EventManagementTeamView> team, boolean sendRolesMap) {
        EventManagementBranch eventManagementBranch = new EventManagementBranch();
        eventManagementBranch.setId(branch.getId());
        eventManagementBranch.setName(branch.getName());

        if (team == null) {
            return eventManagementBranch;
        }

        EventManagementTeam cogEventTeam = new EventManagementTeam();
        cogEventTeam.setEventCategoryName(defaultCategoryName);
        cogEventTeam.setEventCategoryId(1);
        cogEventTeam.setBranchId(branch.getId());
        cogEventTeam.setBranchName(branch.getName());
        if (sendRolesMap) {
            cogEventTeam.setRolesMap(Stream.of(
                new String[]{"eventAdmin", EApplicationRole.EVENT_DEFAULT_ADMIN.getValue()},
                new String[]{"eventAssistant", EApplicationRole.EVENT_DEFAULT_ASSISTANT.getValue()},
                new String[]{"eventAudioVisual", EApplicationRole.EVENT_DEFAULT_AUDIO_VISUAL.getValue()},
                new String[]{"eventGa", EApplicationRole.EVENT_DEFAULT_GA.getValue()},
                new String[]{"eventGraphicDesigner", EApplicationRole.EVENT_DEFAULT_GRAPHIC_DESIGNER.getValue()},
                new String[]{"eventManager", EApplicationRole.EVENT_DEFAULT_MANAGER.getValue()},
                new String[]{"eventPhotographer", EApplicationRole.EVENT_DEFAULT_PHOTOGRAPHER.getValue()},
                new String[]{"eventPublicRelations", EApplicationRole.EVENT_DEFAULT_PUBLIC_RELATIONS.getValue()},
                new String[]{"eventRegistrar", EApplicationRole.EVENT_DEFAULT_REGISTRAR.getValue()},
                new String[]{"eventRepresentative", EApplicationRole.EVENT_DEFAULT_REPRESENTATIVE.getValue()},
                new String[]{"eventSpokesperson", EApplicationRole.EVENT_DEFAULT_SPOKESPERSON.getValue()},
                new String[]{"eventVideographer", EApplicationRole.EVENT_DEFAULT_VIDEOGRAPHER.getValue()}
            ).collect(Collectors.toMap(data -> data[0], data -> data[1])));
        }

        EventManagementTeam asezEventTeam = new EventManagementTeam();
        asezEventTeam.setEventCategoryName("ASEZ / IUBA");
        asezEventTeam.setEventCategoryId(2);
        asezEventTeam.setBranchId(branch.getId());
        asezEventTeam.setBranchName(branch.getName());
        if (sendRolesMap) {
            asezEventTeam.setRolesMap(Stream.of(
                new String[]{"eventAdmin", EApplicationRole.EVENT_ASEZ_IUBA_ADMIN.getValue()},
                new String[]{"eventAssistant", EApplicationRole.EVENT_ASEZ_IUBA_ASSISTANT.getValue()},
                new String[]{"eventAudioVisual", EApplicationRole.EVENT_ASEZ_IUBA_AUDIO_VISUAL.getValue()},
                new String[]{"eventGa", EApplicationRole.EVENT_ASEZ_IUBA_GA.getValue()},
                new String[]{"eventGraphicDesigner", EApplicationRole.EVENT_ASEZ_IUBA_GRAPHIC_DESIGNER.getValue()},
                new String[]{"eventManager", EApplicationRole.EVENT_ASEZ_IUBA_MANAGER.getValue()},
                new String[]{"eventPhotographer", EApplicationRole.EVENT_ASEZ_IUBA_PHOTOGRAPHER.getValue()},
                new String[]{"eventPublicRelations", EApplicationRole.EVENT_ASEZ_IUBA_PUBLIC_RELATIONS.getValue()},
                new String[]{"eventRegistrar", EApplicationRole.EVENT_ASEZ_IUBA_REGISTRAR.getValue()},
                new String[]{"eventRepresentative", EApplicationRole.EVENT_ASEZ_IUBA_REPRESENTATIVE.getValue()},
                new String[]{"eventSpokesperson", EApplicationRole.EVENT_ASEZ_IUBA_SPOKESPERSON.getValue()},
                new String[]{"eventVideographer", EApplicationRole.EVENT_ASEZ_IUBA_VIDEOGRAPHER.getValue()}
            ).collect(Collectors.toMap(data -> data[0], data -> data[1])));
        }

        EventManagementTeam asezWaoEventTeam = new EventManagementTeam();
        asezWaoEventTeam.setEventCategoryName("ASEZ WAO / IWBA");
        asezWaoEventTeam.setEventCategoryId(3);
        asezWaoEventTeam.setBranchId(branch.getId());
        asezWaoEventTeam.setBranchName(branch.getName());
        if (sendRolesMap) {
            asezWaoEventTeam.setRolesMap(Stream.of(
                new String[]{"eventAdmin", EApplicationRole.EVENT_ASEZ_WAO_IWBA_ADMIN.getValue()},
                new String[]{"eventAssistant", EApplicationRole.EVENT_ASEZ_WAO_IWBA_ASSISTANT.getValue()},
                new String[]{"eventAudioVisual", EApplicationRole.EVENT_ASEZ_WAO_IWBA_AUDIO_VISUAL.getValue()},
                new String[]{"eventGa", EApplicationRole.EVENT_ASEZ_WAO_IWBA_GA.getValue()},
                new String[]{"eventGraphicDesigner", EApplicationRole.EVENT_ASEZ_WAO_IWBA_GRAPHIC_DESIGNER.getValue()},
                new String[]{"eventManager", EApplicationRole.EVENT_ASEZ_WAO_IWBA_MANAGER.getValue()},
                new String[]{"eventPhotographer", EApplicationRole.EVENT_ASEZ_WAO_IWBA_PHOTOGRAPHER.getValue()},
                new String[]{"eventPublicRelations", EApplicationRole.EVENT_ASEZ_WAO_IWBA_PUBLIC_RELATIONS.getValue()},
                new String[]{"eventRegistrar", EApplicationRole.EVENT_ASEZ_WAO_IWBA_REGISTRAR.getValue()},
                new String[]{"eventRepresentative", EApplicationRole.EVENT_ASEZ_WAO_IWBA_REPRESENTATIVE.getValue()},
                new String[]{"eventSpokesperson", EApplicationRole.EVENT_ASEZ_WAO_IWBA_SPOKESPERSON.getValue()},
                new String[]{"eventVideographer", EApplicationRole.EVENT_ASEZ_WAO_IWBA_VIDEOGRAPHER.getValue()}
            ).collect(Collectors.toMap(data -> data[0], data -> data[1])));
        }

        Map<Integer, EventManagementTeamView> leaderMap = new HashMap<>();

        // Dao call to get event team members by branch-id
        team.forEach(member -> {
            final String applicationRoleName = member.getApplicationRoleName();
            if (hasEventsAccessInBranch(member, branch)) {
                // Leader
                if (EApplicationRole.BRANCH_ACCESS.is(applicationRoleName)) {
                    if (EApplicationRole.BRANCH_LEADER.is(member.getRoleName())) {
                        leaderMap.put(member.getUserId(), member);
                    }
                }

                // Default Team
                else if (EApplicationRole.EVENT_DEFAULT_ASSISTANT.is(applicationRoleName)) {
                    cogEventTeam.getEventAssistant().add(member);
                } else if (EApplicationRole.EVENT_DEFAULT_AUDIO_VISUAL.is(applicationRoleName)) {
                    cogEventTeam.getEventAudioVisual().add(member);
                } else if (EApplicationRole.EVENT_DEFAULT_GRAPHIC_DESIGNER.is(applicationRoleName)) {
                    cogEventTeam.getEventGraphicDesigner().add(member);
                } else if (EApplicationRole.EVENT_DEFAULT_PHOTOGRAPHER.is(applicationRoleName)) {
                    cogEventTeam.getEventPhotographer().add(member);
                } else if (EApplicationRole.EVENT_DEFAULT_PUBLIC_RELATIONS.is(applicationRoleName)) {
                    cogEventTeam.getEventPublicRelations().add(member);
                } else if (EApplicationRole.EVENT_DEFAULT_REGISTRAR.is(applicationRoleName)) {
                    cogEventTeam.getEventRegistrar().add(member);
                } else if (EApplicationRole.EVENT_DEFAULT_REPRESENTATIVE.is(applicationRoleName)) {
                    cogEventTeam.getEventRepresentative().add(member);
                } else if (EApplicationRole.EVENT_DEFAULT_SPOKESPERSON.is(applicationRoleName)) {
                    cogEventTeam.getEventSpokesperson().add(member);
                } else if (EApplicationRole.EVENT_DEFAULT_VIDEOGRAPHER.is(applicationRoleName)) {
                    cogEventTeam.getEventVideographer().add(member);
                }

                // ASEZ/IUBA Team
                else if (EApplicationRole.EVENT_ASEZ_IUBA_ASSISTANT.is(applicationRoleName)) {
                    asezEventTeam.getEventAssistant().add(member);
                } else if (EApplicationRole.EVENT_ASEZ_IUBA_AUDIO_VISUAL.is(applicationRoleName)) {
                    asezEventTeam.getEventAudioVisual().add(member);
                } else if (EApplicationRole.EVENT_ASEZ_IUBA_GRAPHIC_DESIGNER.is(applicationRoleName)) {
                    asezEventTeam.getEventGraphicDesigner().add(member);
                } else if (EApplicationRole.EVENT_ASEZ_IUBA_PHOTOGRAPHER.is(applicationRoleName)) {
                    asezEventTeam.getEventPhotographer().add(member);
                } else if (EApplicationRole.EVENT_ASEZ_IUBA_PUBLIC_RELATIONS.is(applicationRoleName)) {
                    asezEventTeam.getEventPublicRelations().add(member);
                } else if (EApplicationRole.EVENT_ASEZ_IUBA_REGISTRAR.is(applicationRoleName)) {
                    asezEventTeam.getEventRegistrar().add(member);
                } else if (EApplicationRole.EVENT_ASEZ_IUBA_REPRESENTATIVE.is(applicationRoleName)) {
                    asezEventTeam.getEventRepresentative().add(member);
                } else if (EApplicationRole.EVENT_ASEZ_IUBA_SPOKESPERSON.is(applicationRoleName)) {
                    asezEventTeam.getEventSpokesperson().add(member);
                } else if (EApplicationRole.EVENT_ASEZ_IUBA_VIDEOGRAPHER.is(applicationRoleName)) {
                    asezEventTeam.getEventVideographer().add(member);
                }

                // ASEZ WAO/IWBA Team
                else if (EApplicationRole.EVENT_ASEZ_WAO_IWBA_ASSISTANT.is(applicationRoleName)) {
                    asezWaoEventTeam.getEventAssistant().add(member);
                } else if (EApplicationRole.EVENT_ASEZ_WAO_IWBA_AUDIO_VISUAL.is(applicationRoleName)) {
                    asezWaoEventTeam.getEventAudioVisual().add(member);
                } else if (EApplicationRole.EVENT_ASEZ_WAO_IWBA_GRAPHIC_DESIGNER.is(applicationRoleName)) {
                    asezWaoEventTeam.getEventGraphicDesigner().add(member);
                } else if (EApplicationRole.EVENT_ASEZ_WAO_IWBA_PHOTOGRAPHER.is(applicationRoleName)) {
                    asezWaoEventTeam.getEventPhotographer().add(member);
                } else if (EApplicationRole.EVENT_ASEZ_WAO_IWBA_PUBLIC_RELATIONS.is(applicationRoleName)) {
                    asezWaoEventTeam.getEventPublicRelations().add(member);
                } else if (EApplicationRole.EVENT_ASEZ_WAO_IWBA_REGISTRAR.is(applicationRoleName)) {
                    asezWaoEventTeam.getEventRegistrar().add(member);
                } else if (EApplicationRole.EVENT_ASEZ_WAO_IWBA_REPRESENTATIVE.is(applicationRoleName)) {
                    asezWaoEventTeam.getEventRepresentative().add(member);
                } else if (EApplicationRole.EVENT_ASEZ_WAO_IWBA_SPOKESPERSON.is(applicationRoleName)) {
                    asezWaoEventTeam.getEventSpokesperson().add(member);
                } else if (EApplicationRole.EVENT_ASEZ_WAO_IWBA_VIDEOGRAPHER.is(applicationRoleName)) {
                    asezWaoEventTeam.getEventVideographer().add(member);
                }
            }

            // Global positions
            if (EApplicationRole.EVENT_DEFAULT_ADMIN.is(applicationRoleName)) {
                cogEventTeam.getEventAdmin().add(member);
            } else if (EApplicationRole.EVENT_ASEZ_IUBA_ADMIN.is(applicationRoleName)) {
                asezEventTeam.getEventAdmin().add(member);
            } else if (EApplicationRole.EVENT_ASEZ_WAO_IWBA_ADMIN.is(applicationRoleName)) {
                asezWaoEventTeam.getEventAdmin().add(member);
            } else if (EApplicationRole.EVENT_DEFAULT_GA.is(applicationRoleName)) {
                cogEventTeam.getEventGa().add(member);
            } else if (EApplicationRole.EVENT_ASEZ_IUBA_GA.is(applicationRoleName)) {
                asezEventTeam.getEventGa().add(member);
            } else if (EApplicationRole.EVENT_ASEZ_WAO_IWBA_GA.is(applicationRoleName)) {
                asezWaoEventTeam.getEventGa().add(member);
            } else if (EApplicationRole.EVENT_DEFAULT_MANAGER.is(applicationRoleName)) {
                cogEventTeam.getEventManager().add(member);
            } else if (EApplicationRole.EVENT_ASEZ_IUBA_MANAGER.is(applicationRoleName)) {
                asezEventTeam.getEventManager().add(member);
            } else if (EApplicationRole.EVENT_ASEZ_WAO_IWBA_MANAGER.is(applicationRoleName)) {
                asezWaoEventTeam.getEventManager().add(member);
            }
        });

        leaderMap.values().forEach(leader -> {
            EventManagementTeamView leaderView = new EventManagementTeamView();
            leaderView.setDisplayName(leader.getDisplayName());
            leaderView.setPictureThumbnailUrl(leader.getPictureThumbnailUrl());
            leaderView.setUserId(leader.getUserId());
            eventManagementBranch.getLeaders().add(leaderView);
        });

        eventManagementBranch.getTeams().add(cogEventTeam);
        eventManagementBranch.getTeams().add(asezWaoEventTeam);
        eventManagementBranch.getTeams().add(asezEventTeam);

        return eventManagementBranch;
    }

    public EventManagementBranch getEventTeamByBranch(Integer branchId) throws NotFoundException {
        Optional<EventBranchView> eventBranchViewOptional = eventBranchViewDao.findById(branchId);

        if (eventBranchViewOptional.isPresent()) {
            EventBranchView eventBranchView = eventBranchViewOptional.get();
            List<EventManagementTeamView> eventManagementTeam = eventManagementTeamViewDao.
                getEventManagementTeamViewsByBranchIdOrApplicationRoleNameContainingOrApplicationRoleNameOrApplicationRoleNameContainingOrApplicationRoleNameContainingOrApplicationRoleNameContainingOrApplicationRoleNameContainingOrApplicationRoleNameContainingOrApplicationRoleNameContainingOrApplicationRoleNameContaining(
                    branchId,
                    EApplicationRole.EVENT_DEFAULT_ADMIN.getValue(),
                    EApplicationRole.EVENT_DEFAULT_MANAGER.getValue(),
                    EApplicationRole.EVENT_ASEZ_IUBA_ADMIN.getValue(),
                    EApplicationRole.EVENT_ASEZ_IUBA_MANAGER.getValue(),
                    EApplicationRole.EVENT_ASEZ_WAO_IWBA_ADMIN.getValue(),
                    EApplicationRole.EVENT_ASEZ_WAO_IWBA_MANAGER.getValue(),
                    EApplicationRole.EVENT_DEFAULT_GA.getValue(),
                    EApplicationRole.EVENT_ASEZ_IUBA_GA.getValue(),
                    EApplicationRole.EVENT_ASEZ_WAO_IWBA_GA.getValue()
                );
            return getEventTeamMembers(eventBranchView, eventManagementTeam, true);
        }
        throw new NotFoundException();
    }

    public EventManagementBranch getEventTeamByEventProposal(Integer eventProposalId) throws NotFoundException {
        Optional<EventProposal> eventProposalOptional = eventProposalsDao.findById(eventProposalId);

        if (eventProposalOptional.isPresent()) {
            EventProposal eventProposal = eventProposalOptional.get();
            if (eventProposal.getBranchId() != null) {
                return getEventTeamByBranch(eventProposal.getBranchId());
            }
        }

        throw new NotFoundException();
    }

    private boolean hasEventsAccessInBranch(EventManagementTeamView teamMemberView, EventBranchView branch) {
        if (branch.getId() == null) {
            return false;
        }
        return (teamMemberView.getBranchId() != null && teamMemberView.getBranchId().equals(branch.getId())) ||
            (teamMemberView.getApplicationRoleReferenceId() != null && teamMemberView.getApplicationRoleReferenceId().equals(branch.getId()));
    }
}
