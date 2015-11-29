package ee.v22.service;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import ee.v22.dao.TagDAO;
import ee.v22.model.Tag;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(EasyMockRunner.class)
public class TagServiceTest {

    @TestSubject
    private TagService tagService = new TagService();

    @Mock
    private TagDAO tagDAO;

    @Test
    public void get() {
        String name = "tere";
        Tag tag = new Tag();
        tag.setId(123L);
        tag.setName(name);

        expect(tagDAO.findTagByName(name)).andReturn(tag);

        replay(tagDAO);

        Tag result = tagService.getTagByName(name);

        verify(tagDAO);

        assertEquals(tag, result);
    }

}
