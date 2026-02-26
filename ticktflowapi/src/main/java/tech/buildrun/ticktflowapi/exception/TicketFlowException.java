package tech.buildrun.ticktflowapi.exception;

import org.springframework.http.ProblemDetail;

public abstract class TicketFlowException extends  RuntimeException {
    public TicketFlowException(String message) {
        super(message);
    }

    public TicketFlowException(Throwable cause) {
        super(cause);
    }

    public ProblemDetail toProblemDetail(){
        var pd = ProblemDetail.forStatus(500);
        pd.setTitle("TicketFlow Internal Server Error");
        pd.setDetail("Contact TicketFlow support");
        return pd;
    }

}