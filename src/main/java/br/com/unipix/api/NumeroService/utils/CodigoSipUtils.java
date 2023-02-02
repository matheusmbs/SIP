package br.com.unipix.api.NumeroService.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import br.com.unipix.api.NumeroService.model.CodigoSip;

public class CodigoSipUtils {
    public static CodigoSip getCodigoSipByMenssagem(String mensagem) {
        List<CodigoSip> codigoSipList = new ArrayList<>();
        codigoSipList.add(new CodigoSip(100, "Trying"));
        codigoSipList.add(new CodigoSip(180, "Ringing"));
        codigoSipList.add(new CodigoSip(181, "Call Is Being Forwarded"));
        codigoSipList.add(new CodigoSip(182, "Queued"));
        codigoSipList.add(new CodigoSip(183, "Session Progress"));
        codigoSipList.add(new CodigoSip(199, "Early Dialog Terminated"));
        codigoSipList.add(new CodigoSip(200, "OK"));
        codigoSipList.add(new CodigoSip(202, "Accepted"));
        codigoSipList.add(new CodigoSip(204, "No Notification"));
        codigoSipList.add(new CodigoSip(300, "Multiple Choices"));
        codigoSipList.add(new CodigoSip(301, "Moved Permanently"));
        codigoSipList.add(new CodigoSip(302, "Moved Temporarily"));
        codigoSipList.add(new CodigoSip(305, "Use Proxy"));
        codigoSipList.add(new CodigoSip(380, "Alternative Service"));
        codigoSipList.add(new CodigoSip(400, "Bad Request"));
        codigoSipList.add(new CodigoSip(401, "Unauthorized"));
        codigoSipList.add(new CodigoSip(402, "Payment Required"));
        codigoSipList.add(new CodigoSip(403, "Forbidden"));
        codigoSipList.add(new CodigoSip(404, "Not Found"));
        codigoSipList.add(new CodigoSip(405, "Method Not Allowed"));
        codigoSipList.add(new CodigoSip(406, "Not Acceptable"));
        codigoSipList.add(new CodigoSip(407, "Proxy Authentication Required"));
        codigoSipList.add(new CodigoSip(408, "Request Timeout"));
        codigoSipList.add(new CodigoSip(409, "Conflict"));
        codigoSipList.add(new CodigoSip(410, "Gone"));
        codigoSipList.add(new CodigoSip(411, "Length Required"));
        codigoSipList.add(new CodigoSip(412, "Conditional Request Failed"));
        codigoSipList.add(new CodigoSip(413, "Request Entity Too Large"));
        codigoSipList.add(new CodigoSip(414, "Request URI Too Long"));
        codigoSipList.add(new CodigoSip(415, "Unsupported Media Type"));
        codigoSipList.add(new CodigoSip(416, "Unsupported URI Scheme"));
        codigoSipList.add(new CodigoSip(417, "Uknown Resource"));
        codigoSipList.add(new CodigoSip(420, "Bad Extension"));
        codigoSipList.add(new CodigoSip(421, "Extension Required"));
        codigoSipList.add(new CodigoSip(422, "Session Interval Too Small"));
        codigoSipList.add(new CodigoSip(423, "Interval Too Brief"));
        codigoSipList.add(new CodigoSip(424, "Bad Location Information"));
        codigoSipList.add(new CodigoSip(428, "Use Identity Header"));
        codigoSipList.add(new CodigoSip(429, "Provide Referrer Identity"));
        codigoSipList.add(new CodigoSip(430, "Flow Failed"));
        codigoSipList.add(new CodigoSip(433, "Anonymity Disallowed"));
        codigoSipList.add(new CodigoSip(436, "Bad Identity Info"));
        codigoSipList.add(new CodigoSip(437, "Unsupported Certificate"));
        codigoSipList.add(new CodigoSip(438, "Invalid Identity Header"));
        codigoSipList.add(new CodigoSip(439, "First Hop Lacks Outbound Support"));
        codigoSipList.add(new CodigoSip(440, "Max"));
        codigoSipList.add(new CodigoSip(469, "Bad Info Package"));
        codigoSipList.add(new CodigoSip(470, "Consent Needed"));
        codigoSipList.add(new CodigoSip(480, "Temporarily Unavailable"));
        codigoSipList.add(new CodigoSip(481, "Call/Transaction Does Not Exist"));
        codigoSipList.add(new CodigoSip(482, "Loop Detected"));
        codigoSipList.add(new CodigoSip(483, "Too Many Hops"));
        codigoSipList.add(new CodigoSip(484, "Address Incomplete"));
        codigoSipList.add(new CodigoSip(485, "Ambiguous"));
        codigoSipList.add(new CodigoSip(486, "Busy Here"));
        codigoSipList.add(new CodigoSip(487, "Request Terminated"));
        codigoSipList.add(new CodigoSip(488, "Not Acceptable Here"));
        codigoSipList.add(new CodigoSip(489, "Bad Event"));
        codigoSipList.add(new CodigoSip(491, "Request Pending"));
        codigoSipList.add(new CodigoSip(493, "Undecipherable"));
        codigoSipList.add(new CodigoSip(494, "Security Agreement Required"));
        codigoSipList.add(new CodigoSip(500, "Server Internal Error"));
        codigoSipList.add(new CodigoSip(501, "Not Implemented"));
        codigoSipList.add(new CodigoSip(502, "Bad Gateway"));
        codigoSipList.add(new CodigoSip(503, "Service Unavailable"));
        codigoSipList.add(new CodigoSip(504, "Server Time"));
        codigoSipList.add(new CodigoSip(505, "Version Not Supported"));
        codigoSipList.add(new CodigoSip(513, "Message Too Large"));
        codigoSipList.add(new CodigoSip(555, "Push Notification Service Not Supported"));
        codigoSipList.add(new CodigoSip(580, "Precondition Failure"));
        codigoSipList.add(new CodigoSip(600, "Busy Everywhere"));
        codigoSipList.add(new CodigoSip(603, "Decline"));
        codigoSipList.add(new CodigoSip(604, "Does Not Exist Anywhere"));
        codigoSipList.add(new CodigoSip(606, "Not Acceptable"));
        codigoSipList.add(new CodigoSip(607, "Unwanted"));

        if(mensagem.equals("bye Maximum call time exceeded")){
            CodigoSip codigoSip = new CodigoSip(200, "Ok");
            return codigoSip;
        }else{
            Optional<CodigoSip> codigoSipOptional = codigoSipList.stream().filter(c -> c.mensagem.equals(mensagem)).findFirst();
            if(codigoSipOptional.isPresent()){
                return codigoSipOptional.get();
            }else{
                CodigoSip codigoSip = new CodigoSip(-1, mensagem);
                return codigoSip;
            }
        }
    }

}
