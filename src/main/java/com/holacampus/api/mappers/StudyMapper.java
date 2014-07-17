/*
 * Copyright (C) 2014 Mikel Corcuera <mik.corcuera@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.holacampus.api.mappers;

import com.holacampus.api.domain.Study;
import com.holacampus.api.domain.University;
import com.holacampus.api.domain.User;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

/**
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public interface StudyMapper {
    
    public List<Study>   getStudies( @Param("university") University university, @Param("q") String q, RowBounds rb) throws Exception;
    
    public List<Study>   getStudies( @Param("university") University university, @Param("q") String q) throws Exception;
    
    public int          getTotalStudies( @Param("university") University university, @Param("q") String q) throws Exception;
    
    public List<User>   getStudents( @Param("university") University university, @Param( "studyId") Long studyId, @Param("q") String q, RowBounds rb) throws Exception;
    
    public List<User>   getStudents( @Param("university") University university, @Param( "studyId") Long studyId, @Param("q") String q) throws Exception;
    
    public int          getTotalStudents( @Param("university") University university, @Param( "studyId") Long studyId, @Param("q") String q) throws Exception;
    
    public int          createStudy( @Param("university") University university, @Param("study") Study study) throws Exception;
    
    public Study         getStudy( @Param("university") University university, @Param( "studyId") Long studyId) throws Exception;
    
    public int          updateStudy( @Param("university") University university, @Param("study") Study study) throws Exception;
    
    public int          deleteStudy( @Param("university") University university, @Param( "studyId") Long studyId) throws Exception;
}
