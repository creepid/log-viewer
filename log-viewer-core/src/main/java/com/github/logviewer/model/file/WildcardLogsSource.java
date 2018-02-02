package com.github.logviewer.model.file;

/**
 * Created by rusakovich on 23.10.2017.
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.logviewer.model.Log;
import com.github.logviewer.model.LogRawAccessor;
import com.github.logviewer.model.Navigation;
import com.github.logviewer.model.support.BaseLogsSource;
import com.github.logviewer.model.support.ByteLogAccess;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import com.fasterxml.jackson.annotation.JsonProperty;


@Component
public class WildcardLogsSource extends BaseLogsSource<ByteLogAccess> {
    private static Logger logger = LoggerFactory.getLogger(WildcardLogsSource.class);

    private LogRawAccessor<ByteLogAccess, FileLog> logAccessAdapter;

    private String pattern;

    /**
     * @return the baseDir
     */
    @Deprecated
    public String getBaseDir() {
        return null;
    }

    /**
     * @param baseDir the baseDir to set
     */
    @Deprecated
    public void setBaseDir(final String baseDir) {
        // NOP
    }

    /**
     * @return the pattern
     */
    @JsonProperty
    @NotEmpty
    public String getPattern() {
        return pattern;
    }

    /**
     * @param pattern the pattern to set
     */
    public void setPattern(final String pattern) {
        this.pattern = FilenameUtils.separatorsToUnix(pattern);
    }

    @Override
    public List<Log> getLogs() throws IOException {
        final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        resolver.setPathMatcher(new AntPathMatcher());
        final Resource[] resources = resolver.getResources("file:" + getPattern());
        final ArrayList<Log> logs = new ArrayList<Log>(resources.length);
        // TODO Decouple direct file log association
        for (int i = 0; i < resources.length; i++) {
            if (resources[i].exists()) {
                if (resources[i].getFile().isFile()) {
                    logs.add(new FileLog(resources[i].getFile()));
                }
            } else {
                logger.info("Ignore not existent file: {}", resources[i].getFile());
            }
        }
        return logs;
    }

    @Override
    public Log getLog(final String path) throws IOException {
        final File f = new File(path);
        if (f.exists()) {
            return new FileLog(f);
        } else {
            return null;
        }
    }

    @Override
    public ByteLogAccess getLogAccess(final Log origLog) throws IOException {
        final FileLog log = (FileLog) getLog(origLog.getPath());
        if (log != null) {
            return getLogAccessAdapter() != null
                    ? getLogAccessAdapter().getLogAccess(log)
                    : new DirectFileLogAccess(log);
        } else {
            return null;
        }
    }

    public LogRawAccessor<ByteLogAccess, FileLog> getLogAccessAdapter() {
        return logAccessAdapter;
    }

    public void setLogAccessAdapter(final LogRawAccessor<ByteLogAccess, FileLog> logAccessAdapter) {
        this.logAccessAdapter = logAccessAdapter;
    }

    @Override
    public Navigation.NavigationType getNavigationType() {
        return Navigation.NavigationType.BYTE;
    }
}
