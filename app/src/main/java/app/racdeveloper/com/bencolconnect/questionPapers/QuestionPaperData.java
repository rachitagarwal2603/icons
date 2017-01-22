package app.racdeveloper.com.bencolconnect.questionPapers;

/**
 * Created by Rachit on 10/19/2016.
 */
public class QuestionPaperData {

        private int id;
        private String papername,papercode,papercontributor,paperurl;

        public QuestionPaperData(int id, String papercode, String papername, String paperurl, String papercontributor) {
            this.id = id;
            this.papercode = papercode;
            this.papername = papername;
            this.papercontributor = papercontributor;
            this.paperurl = paperurl;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPapername() {
            return papername;
        }

        public void setPapername(String papername) {
            this.papername = papername;
        }

        public String getPapercode() {
            return papercode;
        }

        public void setPapercode(String papercode) {
            this.papercode = papercode;
        }

        public String getPapercontributor() {
            return papercontributor;
        }

        public void setPapercontributor(String papercontributor) {
            this.papercontributor = papercontributor;
        }

        public String getPaperurl() {
            return paperurl;
        }

        public void setPaperurl(String paperurl) {
            this.paperurl = paperurl;
        }

}
