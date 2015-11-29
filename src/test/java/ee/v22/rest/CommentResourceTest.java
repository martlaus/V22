package ee.v22.rest;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import ee.v22.model.Portfolio;
import org.junit.Test;

import ee.v22.common.test.ResourceIntegrationTestBase;
import ee.v22.model.Comment;
import ee.v22.rest.CommentResource.AddCommentForm;

public class CommentResourceTest extends ResourceIntegrationTestBase {

    private static final String POST_COMMENT_PORTFOLIO_URL = "comment/portfolio";

    @Test
    public void addPortfolioComment() {
        login("39011220011");

        AddCommentForm addCommentForm = new AddCommentForm();

        Portfolio portfolio = new Portfolio();
        long portfolioId = 1L;
        portfolio.setId(portfolioId);
        addCommentForm.setPortfolio(portfolio);

        Comment comment = new Comment();
        String commentText = "This is my comment. Very nice one! :)";
        comment.setText(commentText);
        addCommentForm.setComment(comment);

        Response response = doPost(POST_COMMENT_PORTFOLIO_URL,
                Entity.entity(addCommentForm, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void addPortfolioCommentNotLoggedIn() {
        AddCommentForm addCommentForm = new AddCommentForm();

        Portfolio portfolio = new Portfolio();
        portfolio.setId(1L);
        addCommentForm.setPortfolio(portfolio);

        Comment comment = new Comment();
        comment.setText("This is my comment. Very nice one! :)");
        addCommentForm.setComment(comment);

        Response response = doPost(POST_COMMENT_PORTFOLIO_URL,
                Entity.entity(addCommentForm, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }

}
