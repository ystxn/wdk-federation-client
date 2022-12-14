package com.symphony.devsol.federation.activity;

import com.symphony.bdk.workflow.engine.executor.ActivityExecutor;
import com.symphony.bdk.workflow.engine.executor.ActivityExecutorContext;
import com.symphony.devsol.federation.client.FederationClient;
import com.symphony.devsol.federation.model.AddRoomMemberRequest;
import com.symphony.devsol.federation.model.BulkAddRoomMemberEntry;
import com.symphony.devsol.federation.model.BulkAddRoomMemberRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddConnectRoomMemberExecutor implements ActivityExecutor<AddConnectRoomMember> {
  private final FederationClient client;

  @Override
  public void execute(ActivityExecutorContext<AddConnectRoomMember> context) {
    AddConnectRoomMember activity = context.getActivity();

    if (activity.getMemberSymphonyIds() != null) {
      List<BulkAddRoomMemberEntry> requests = activity.getMemberSymphonyIds().stream()
          .map(id -> BulkAddRoomMemberEntry.builder().streamId(activity.getStreamId()).memberSymphonyId(id).build())
          .toList();
      BulkAddRoomMemberRequest request = BulkAddRoomMemberRequest.builder()
          .requests(requests)
          .advisorSymphonyId(activity.getAdvisorSymphonyId())
          .externalNetwork(activity.getExternalNetwork())
          .build();
      context.setOutputVariable("result", client.addRoomMember(request));
    } else {
      AddRoomMemberRequest request = AddRoomMemberRequest.builder()
          .advisorSymphonyId(activity.getAdvisorSymphonyId())
          .memberSymphonyId(activity.getMemberSymphonyId())
          .externalNetwork(activity.getExternalNetwork())
          .contact(activity.isContact())
          .build();
      context.setOutputVariable("result", client.addRoomMember(activity.getStreamId(), request));
    }
  }
}
