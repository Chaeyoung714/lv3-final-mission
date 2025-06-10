package finalmission.controller;

import finalmission.dto.LoginMemberInfo;
import finalmission.dto.ReservationCreateRequest;
import finalmission.dto.ReservationFullResponse;
import finalmission.dto.annotation.CurrentMember;
import finalmission.entity.Member;
import finalmission.service.ReservationService;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationFullResponse postReservation(
            @RequestBody ReservationCreateRequest request,
            @CurrentMember LoginMemberInfo loginMemberInfo
            ) {
        return reservationService.createReservation(
                request, loginMemberInfo
        );
    }
}
