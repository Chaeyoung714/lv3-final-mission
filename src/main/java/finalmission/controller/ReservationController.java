package finalmission.controller;

import finalmission.dto.LoginMemberInfo;
import finalmission.dto.ReservationCreateRequest;
import finalmission.dto.ReservationFullResponse;
import finalmission.dto.ReservationSimpleResponse;
import finalmission.dto.annotation.CurrentMember;
import finalmission.service.ReservationService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationFullResponse> getAllReservations() {
        return reservationService.findAll();
    }

    @GetMapping("/reservations/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReservationFullResponse getMyReservation(
            @PathVariable Long id,
            @CurrentMember LoginMemberInfo loginMemberInfo
    ) {
        return reservationService.findMyReservationById(loginMemberInfo, id);
    }

    @GetMapping("/reservations/mine")
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationFullResponse> getMyAllReservations(
            @CurrentMember LoginMemberInfo loginMemberInfo
    ) {
        return reservationService.findMyReservations(loginMemberInfo);
    }

    @PostMapping("/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationSimpleResponse postReservation(
            @RequestBody ReservationCreateRequest request,
            @CurrentMember LoginMemberInfo loginMemberInfo
    ) {
        return reservationService.createReservation(
                request, loginMemberInfo
        );
    }
}
