package analyzer.detectors.orm;

import analyzer.detectors.commons.AbstractBytecodeFileTracingDetector;
import analyzer.detectors.spring.config.commons.BugType;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;

import javax.naming.OperationNotSupportedException;

/**
 * Created by NM.Rabotaev on 18.05.2017.
 */
public class ManyEagerFieldsDetector extends AbstractBytecodeFileTracingDetector {
    public ManyEagerFieldsDetector(BugReporter reporter) {
        super(reporter);
    }

    @Override
    protected void reportBug() throws OperationNotSupportedException {

    }

    @Override
    protected BugInstance createBug() throws OperationNotSupportedException {
        return new BugInstance(this, BugType.TOO_MANY_EAGER_FIELDS.getValue(), LOW_PRIORITY)
                .addClass(this);
    }
}
