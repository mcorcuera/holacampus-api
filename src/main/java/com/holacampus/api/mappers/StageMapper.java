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

import com.holacampus.api.domain.Stage;
import com.holacampus.api.domain.User;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

/**
 *
 * @author Mikel Corcuera <mik.corcuera@gmail.com>
 */
public interface StageMapper {
    
    public List<Stage>      getStages( @Param("user") User user, RowBounds rb) throws Exception;
    
    public List<Stage>      getStages( @Param("user") User user) throws Exception;
    
    public int              getTotalStages( @Param("user") User user) throws Exception;
    
    public int              createStage( @Param("user") User user, @Param("stage") Stage stage) throws Exception;
    
    public Stage            getStage( @Param("user") User user, @Param( "stageId") Long stageId) throws Exception;
    
    public int              updateStage( @Param("user") User User, @Param("stage") Stage stage) throws Exception;
    
    public int              deleteStage( @Param("user") User user, @Param( "stageId") Long stageId) throws Exception;
}
