package ee.v22.rest;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import ee.v22.model.Comment;
import ee.v22.model.Portfolio;
import ee.v22.service.PortfolioService;

@Path("comment")
@RolesAllowed("USER")
public class CommentResource extends BaseResource {

    @Inject
    private PortfolioService portfolioService;

    @POST
    @Path("portfolio")
    @Consumes("application/json")
    public void addPortfolioComment(AddCommentForm form) {

        Comment comment = form.getComment();
        comment.setCreator(getLoggedInUser());

        portfolioService.addComment(comment, form.getPortfolio());
    }

    public static class AddCommentForm {

        private Comment comment;
        private Portfolio portfolio;

        public Comment getComment() {
            return comment;
        }

        public void setComment(Comment comment) {
            this.comment = comment;
        }

        public Portfolio getPortfolio() {
            return portfolio;
        }

        public void setPortfolio(Portfolio portfolio) {
            this.portfolio = portfolio;
        }
    }

}
