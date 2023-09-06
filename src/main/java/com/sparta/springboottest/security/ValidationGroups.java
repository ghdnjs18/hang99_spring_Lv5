package com.sparta.springboottest.security;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

public class ValidationGroups {
    public interface SizeCheckGroup { }
    public interface PatternCheckGroup { }

    @GroupSequence({SizeCheckGroup.class, PatternCheckGroup.class, Default.class})
    public interface ValidationSequence {
    }
}
