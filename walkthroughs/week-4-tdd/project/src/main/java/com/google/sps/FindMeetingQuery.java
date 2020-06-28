// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(final Collection<Event> events, final MeetingRequest request) {
    if (events.isEmpty() && request.getAttendees().isEmpty()) {
      return Arrays.asList(TimeRange.WHOLE_DAY);
    }

    if (request.getDuration() > TimeRange.WHOLE_DAY.duration()) {
      return Arrays.asList();
    }

    List<TimeRange> impossbleTimeRanges = new ArrayList<>();
    for (final Event event : events) {
      final Set<String> attendeeIntersection = new HashSet<>(event.getAttendees());
      attendeeIntersection.retainAll(request.getAttendees());
      if (attendeeIntersection.size() != 0) {
        impossbleTimeRanges.add(event.getWhen());
      }
    }
    Collections.sort(impossbleTimeRanges, TimeRange.ORDER_BY_START);

    List<TimeRange> freeTimeRanges = findFreeTimeRangesIn(impossbleTimeRanges, TimeRange.START_OF_DAY,
        TimeRange.END_OF_DAY);
    List<TimeRange> result = freeTimeRanges.stream().filter(t -> t.duration() >= request.getDuration())
        .collect(Collectors.toList());

    return result;
  }

  private List<TimeRange> findFreeTimeRangesIn(List<TimeRange> impossibleTimeRanges, int start, int end) {
    if (impossibleTimeRanges.size() == 0) {
      return new ArrayList<>(Arrays.asList(TimeRange.fromStartEnd(start, end, true)));
    } else if (impossibleTimeRanges.size() == 1) {
      if (impossibleTimeRanges.get(0).start() > start) {
        return new ArrayList<>(Arrays.asList(TimeRange.fromStartEnd(start, impossibleTimeRanges.get(0).start(), false),
            TimeRange.fromStartEnd(impossibleTimeRanges.get(0).end(), end, true)));
      } else {
        if (impossibleTimeRanges.get(0).end() <= start) {
          return new ArrayList<>(Arrays.asList(TimeRange.fromStartEnd(start, end, true)));
        } else {
          return new ArrayList<>(Arrays.asList(TimeRange.fromStartEnd(impossibleTimeRanges.get(0).end(), end, true)));
        }
      }
    } else {
      TimeRange curr = impossibleTimeRanges.get(0);
      if (curr.start() <= start) {
        return findFreeTimeRangesIn(impossibleTimeRanges.subList(1, impossibleTimeRanges.size()), curr.end(), end);
      } else {
        List<TimeRange> freeTimeRanges = new ArrayList<>(
            Arrays.asList(TimeRange.fromStartEnd(start, curr.start(), false)));
        freeTimeRanges.addAll(findFreeTimeRangesIn(impossibleTimeRanges, curr.start(), end));
        return freeTimeRanges;
      }
    }
  }
}
