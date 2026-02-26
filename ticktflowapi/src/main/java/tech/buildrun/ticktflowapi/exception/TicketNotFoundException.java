package tech.buildrun.ticktflowapi.exception;

import org.springframework.http.ProblemDetail;

public class TicketNotFoundException extends TicketFlowException {
    public TicketNotFoundException() {
        super("Ticket not found");
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(404);
        pd.setTitle("Ticket Not Found");
        pd.setDetail("The requested ticket does not exist");
        return pd;
    }
}
