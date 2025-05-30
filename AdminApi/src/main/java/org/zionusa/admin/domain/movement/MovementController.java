package org.zionusa.admin.domain.movement;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.zionusa.admin.domain.*;
import org.zionusa.base.controller.BaseController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/movements")
public class MovementController extends BaseController<Movement, Integer> {

    private final MovementService service;

    @Autowired
    public MovementController(MovementService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/current")
    @ApiOperation(value = "View the current active movement")
    public Movement getCurrentMovement() {
        return service.getCurrentMovement();
    }

    @GetMapping("/{movementId}/user/{userId}")
    @ApiOperation(value = "View the count of activity logs per activity in the movement for a user")
    public List<MovementUserActivity> getUserMovementActivityReport(@PathVariable Integer movementId, @PathVariable Integer userId) {
        return service.getMovementUserActivity(movementId, userId);
    }

    @GetMapping("/{movementId}/group/{groupId}")
    @ApiOperation(value = "View the count of activity logs per activity in the movement for a group")
    public List<MovementGroupActivity> getGroupMovementActivityReport(@PathVariable Integer movementId, @PathVariable Integer groupId) {
        return service.getMovementGroupActivity(movementId, groupId);
    }

    @GetMapping("/{movementId}/church/{churchId}")
    @ApiOperation(value = "View the count of activity logs per activity in the movement for a church")
    public List<MovementChurchActivity> getChurchMovementActivityReport(@PathVariable Integer movementId, @PathVariable Integer churchId) {
        return service.getMovementChurchActivity(movementId, churchId);
    }

    @GetMapping("/{movementId}/church/{churchId}/{startDate}/{endDate}")
    public List<MovementChurchDateActivity> getChurchMovementActivityReportByDateBetween(@PathVariable Integer movementId, @PathVariable Integer churchId, @PathVariable String startDate, @PathVariable String endDate) {
        return service.getMovementChurchActivityByDateBetween(movementId, churchId, startDate, endDate);
    }

    @GetMapping("/{movementId}/east-coast")
    public List<MovementEastCoastActivity> getEastCoastMovementActivityReport(@PathVariable Integer movementId) {
        return service.getMovementEastCoastActivity(movementId);
    }

    @GetMapping("/{movementId}/category/east-coast/count")
    @ApiOperation(value = "View the overall movement totals of preaching, signatures, fruit, attendance and t-signatures")
    public MovementUserCategoryCount getEastCoastMovementCategoryCountReport(@PathVariable Integer movementId) {
        return service.getEastCoastMovementCategoryCountReport(movementId);
    }

    @GetMapping("/{movementId}/level/east-coast")
    @ApiOperation(value = "View the level and activity count for the top 300 members, sorted by level and activity count for a movement")
    public List<MovementUserLevelReport> getUserMovementLevelEastCoastReport(HttpServletRequest request, @PathVariable Integer movementId) {
        return service.getUserMovementLevelEastCoastReport(request, movementId);
    }

    @GetMapping("/{movementId}/level/east-coast/counts")
    @ApiOperation(value = "View the number of members in each level")
    public List<MovementUserLevelCount> getUserMovementLevelCountEastCoastReport(@PathVariable Integer movementId) {
        return service.getUserMovementLevelCountEastCoastReport(movementId);
    }

    @GetMapping("/{movementId}/level/church/{churchId}")
    @ApiOperation(value = "View the level and activity count for each member in a church")
    public List<MovementUserLevelReport> getUserMovementLevelChurchReport(HttpServletRequest request, @PathVariable Integer movementId, @PathVariable Integer churchId) {
        return service.getUserMovementLevelChurchReport(request, movementId, churchId);
    }

    @GetMapping("/{movementId}/level/group/{groupId}")
    @ApiOperation(value = "View the level and activity count for each member in a group")
    public List<MovementUserLevelReport> getUserMovementLevelGroupReport(HttpServletRequest request, @PathVariable Integer movementId, @PathVariable Integer groupId) {
        return service.getUserMovementLevelGroupReport(request, movementId, groupId);
    }

    @GetMapping("/{movementId}/level/team/{teamId}")
    @ApiOperation(value = "View the level and activity count for each member in a team")
    public List<MovementUserLevelReport> getUserMovementLevelTeamReport(HttpServletRequest request, @PathVariable Integer movementId, @PathVariable Integer teamId) {
        return service.getUserMovementLevelTeamReport(request, movementId, teamId);
    }

    @GetMapping("/{movementId}/level/user/{userId}")
    @ApiOperation(value = "View the level and activity count for a member")
    public List<MovementUserLevelReport> getUserMovementLevelUserReport(HttpServletRequest request, @PathVariable Integer movementId, @PathVariable Integer userId) {
        return service.getUserMovementLevelUserReport(request, movementId, userId);
    }

    @GetMapping("/{movementId}/branch-baptisms")
    @ApiOperation(value = "List of the branch churches and baptisms for the movement")
    public List<Movement.BranchBaptismsLeaderInputView> getMovementsBranchBaptisms(@PathVariable Integer movementId) {
        return service.getMovementsBranchBaptisms(movementId);
    }

    @GetMapping("/{movementId}/branch-baptism-count/{branchId}")
    @ApiOperation(value = "Branch church baptism number")
    public Integer getMovementsBranchBaptismCount(@PathVariable Integer movementId, @PathVariable Integer branchId) {
        return service.getMovementsBranchBaptismCount(movementId, branchId);
    }

    @GetMapping("/{movementId}/main-branch-baptisms")
    @ApiOperation(value = "List of the main churches and baptisms for the movement")
    public List<Movement.MainBranchBaptismsLeaderInputView> getMovementsMainBranchBaptisms(@PathVariable Integer movementId) {
        return service.getMovementsMainBranchBaptisms(movementId);
    }

    @GetMapping("/{movementId}/main-branch-baptism-count/{branchId}")
    @ApiOperation(value = "Main branch church baptism number")
    public Integer getMovementsMainBranchBaptismCount(@PathVariable Integer movementId, @PathVariable Integer branchId) {
        return service.getMovementsMainBranchBaptismCount(movementId, branchId);
    }

    @GetMapping("/{movementId}/all-baptisms")
    @ApiOperation(value = "Total baptism number for the movement")
    public Integer getTotalMovementBaptisms(@PathVariable Integer movementId) {
        return service.getTotalMovementBaptisms(movementId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{movementId}/group-baptisms/{groupId}/{date}")
    @ApiOperation(value = "Get today's baptism number")
    public Movement.GroupBaptismsLeaderInput getMovementsGroupTodaysBaptisms(@PathVariable Integer movementId,
                                                                               @PathVariable Integer groupId,
                                                                               @PathVariable String date) {
        return service.getMovementsGroupTodaysBaptisms(movementId, groupId, date);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{movementId}/branch-baptisms/{branchId}/{date}")
    @ApiOperation(value = "Get today's baptism number")
    public Movement.BranchBaptismsLeaderInput getMovementsBranchTodaysBaptisms(@PathVariable Integer movementId,
                                                                               @PathVariable Integer branchId,
                                                                               @PathVariable String date) {
        return service.getMovementsBranchTodaysBaptisms(movementId, branchId, date);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/group-baptisms")
    @ApiOperation(value = "Save group baptisms")
    public void saveMovementsGroupBaptisms(@RequestBody Movement.GroupBaptismsLeaderInput groupBaptismsLeaderInput) {
        service.saveGroupBaptismsLeaderInput(groupBaptismsLeaderInput);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/branch-baptisms")
    @ApiOperation(value = "Save branch church baptisms")
    public void saveMovementsBranchBaptisms(@RequestBody Movement.BranchBaptismsLeaderInput branchBaptismsLeaderInput) {
        service.saveBranchBaptismsLeaderInput(branchBaptismsLeaderInput);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{movementId}/main-branch-baptisms/{branchId}/{date}")
    @ApiOperation(value = "Get today's baptism number")
    public Movement.MainBranchBaptismsLeaderInput getMovementsMainBranchTodaysBaptisms(@PathVariable Integer movementId,
                                                                                       @PathVariable Integer branchId,
                                                                                       @PathVariable String date) {
        return service.getMovementsMainBranchTodaysBaptisms(movementId, branchId, date);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/message/{date}")
    @ApiOperation(value = "Get movement message of the day")
    public String getMovementsMessageOfTheDay (@PathVariable String date) {
        return service.getAssociation(date);
    }

    @GetMapping("/all-baptisms/group/{startDate}/{endDate}")
    @ApiOperation(value = "Total baptism number for the movement")
    public List<Movement.GroupBaptismsLeaderInput> getTotalBaptismsByGroup(@PathVariable String startDate,
                                                                        @PathVariable String endDate) {
        return service.getTotalBaptismsByGroup(startDate, endDate);
    }

    @GetMapping("/all-baptisms/branch/{startDate}/{endDate}")
    @ApiOperation(value = "Total baptism number for the movement")
    public List<Movement.BranchBaptismsLeaderInput> getTotalBaptismsByBranch(@PathVariable String startDate, @PathVariable String endDate) {
        return service.getTotalBaptismsByBranch(startDate, endDate);
    }

    @GetMapping("/all-baptisms/main-branch/{startDate}/{endDate}")
    @ApiOperation(value = "Total main baptism number for the movement")
    public List<Movement.MainBranchBaptismsLeaderInput> getTotalBaptismsByMainBranch(@PathVariable String startDate, @PathVariable String endDate) {
        return service.getTotalBaptismsByMainBranch(startDate, endDate);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/main-branch-baptisms")
    @ApiOperation(value = "Save main church baptisms")
    public void saveMovementsMainBranchBaptisms(@RequestBody Movement.MainBranchBaptismsLeaderInput mainBranchBaptismsLeaderInput) {
        service.saveMainBranchBaptismsLeaderInput(mainBranchBaptismsLeaderInput);
    }

}
