package org.jboss.cdi.tck.shrinkwrap;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.jboss.arquillian.container.spi.client.container.DeployableContainer;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.Asset;

/**
 * An asset used to add an Exception to an archive. This is used to propagate exceptions seen during the
 * {@link ArchiveBuilder#processSources(Archive)} phase for use by the Arquillian {@link DeployableContainer}
 * configured by the TCK runner.
 *
 * @see org.jboss.cdi.tck.test.spi.container.MockContainer
 */
public class ExceptionAsset implements Asset {
    final byte[] content = new byte[0];

    private Exception exception;

    public ExceptionAsset(Exception exception) {
        this.exception = exception;
    }

    @Override
    public InputStream openStream() {
        return new ByteArrayInputStream(content);
    }

    public Exception getSource() {
        return exception;
    }
}
