package com.acme.saferoute.platform.fleet.interfaces.rest;

import com.acme.saferoute.platform.fleet.application.commandservices.AssignmentCommandService;
import com.acme.saferoute.platform.fleet.application.queryservices.AssignmentQueryService;
import com.acme.saferoute.platform.fleet.domain.model.queries.GetAssignmentByRouteIdQuery;
import com.acme.saferoute.platform.fleet.interfaces.rest.resources.AssignmentResource;
import com.acme.saferoute.platform.fleet.interfaces.rest.resources.CreateAssignmentResource;
import com.acme.saferoute.platform.fleet.interfaces.rest.transform.AssignmentResourceFromEntityAssembler;
import com.acme.saferoute.platform.fleet.interfaces.rest.transform.CreateAssignmentCommandFromResourceAssembler;
import com.acme.saferoute.platform.shared.application.result.ApplicationError;
import com.acme.saferoute.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import com.acme.saferoute.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller exposing assignment endpoints for the Fleet bounded context (US-6).
 */
@RestController
@RequestMapping(value = "/api/v1/assignments", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Assignments", description = "Route staffing (driver and children) endpoints")
public class AssignmentsController {

    private final AssignmentCommandService assignmentCommandService;
    private final AssignmentQueryService assignmentQueryService;

    /**
     * Creates the controller with the assignment application services.
     *
     * @param assignmentCommandService the assignment command service
     * @param assignmentQueryService   the assignment query service
     */
    public AssignmentsController(AssignmentCommandService assignmentCommandService,
                                 AssignmentQueryService assignmentQueryService) {
        this.assignmentCommandService = assignmentCommandService;
        this.assignmentQueryService = assignmentQueryService;
    }

    /**
     * Assigns a driver and children to a route (US-6).
     *
     * @param resource the create-assignment payload
     * @return the created assignment resource, or an error response
     */
    @PostMapping
    @Operation(summary = "Create an assignment",
            description = "Assigns a driver and a set of children to a route.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Assignment created",
                    content = @Content(schema = @Schema(implementation = AssignmentResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Route not found"),
            @ApiResponse(responseCode = "409", description = "Conflict - route already has an assignment")
    })
    public ResponseEntity<?> createAssignment(@Valid @RequestBody CreateAssignmentResource resource) {
        var command = CreateAssignmentCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = assignmentCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                AssignmentResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED);
    }

    /**
     * Retrieves the assignment for a route.
     *
     * @param routeId the route identifier
     * @return the assignment resource, or a 404 error response
     */
    @GetMapping
    @Operation(summary = "Get assignment by route",
            description = "Retrieves the driver and children assigned to a route.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignment found",
                    content = @Content(schema = @Schema(implementation = AssignmentResource.class))),
            @ApiResponse(responseCode = "404", description = "Assignment not found")
    })
    public ResponseEntity<?> getAssignmentByRoute(
            @Parameter(description = "Route identifier", example = "1", required = true)
            @RequestParam Long routeId) {
        var assignment = assignmentQueryService.handle(new GetAssignmentByRouteIdQuery(routeId));
        if (assignment.isEmpty()) {
            return ErrorResponseAssembler.toErrorResponseFromApplicationError(
                    ApplicationError.notFound("Assignment", routeId.toString()));
        }
        return ResponseEntity.ok(AssignmentResourceFromEntityAssembler.toResourceFromEntity(assignment.get()));
    }
}
