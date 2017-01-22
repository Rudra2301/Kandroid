package in.andres.kandroid;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.andres.kandroid.kanboard.KanboardColumn;
import in.andres.kandroid.kanboard.KanboardDashboard;
import in.andres.kandroid.kanboard.KanboardProject;
import in.andres.kandroid.kanboard.KanboardTask;

/**
 * Created by Thomas Andres on 06.01.17.
 */

public class DashProjectsAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private KanboardDashboard mDashboard;
    private KanboardProject mProject;

    public DashProjectsAdapter(Context context, KanboardDashboard values) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDashboard = values;
        mProject = null;
    }

    public DashProjectsAdapter(Context context, KanboardProject values) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDashboard = null;
        mProject = values;
    }

    @Override
    public int getGroupCount() {
        if (mDashboard != null)
          return mDashboard.getGroupedTasks().size();
        // TODO: Add Swimlanes

        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mDashboard != null)
            return mDashboard.getGroupedTasks().get(mDashboard.getProjects().get(groupPosition).getId()).size();
        // TODO: Add Swimlanes

        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (mDashboard != null)
            return mDashboard.getProjects().get(groupPosition);
        // TODO: Add Swimlanes

        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (mDashboard != null)
            return mDashboard.getGroupedTasks().get(mDashboard.getProjects().get(groupPosition).getId()).get(childPosition);
        // TODO: Add Swimlanes

        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        KanboardProject proj = (KanboardProject) getGroup(groupPosition);

        if (convertView == null)
            convertView = mInflater.inflate(R.layout.listitem_dash_project_header, null);

        TextView projectName = (TextView) convertView.findViewById(R.id.project_name);
        TextView projectDescription = (TextView) convertView.findViewById(R.id.project_description);
        TextView projectColumns = (TextView) convertView.findViewById(R.id.project_columns);
        TextView projectNbTasks = (TextView) convertView.findViewById(R.id.project_nb_own_tasks);

        projectName.setText(proj.getName());
        if ((proj.getDescription() == null) || proj.getDescription().contentEquals(""))
            projectDescription.setVisibility(View.GONE);
        projectDescription.setText(proj.getDescription());
        List<String> columns = new ArrayList<>();
        for (KanboardColumn c: proj.getColumns())
            columns.add(String.format("<big><b>%1s</b></big> %2s", c.getNumberTasks(), c.getTitle()));
        projectColumns.setText(Html.fromHtml(TextUtils.join(" <big><b>|</b></big> ", columns)));
        projectNbTasks.setText(mContext.getResources().getQuantityString(R.plurals.format_nb_tasks, getChildrenCount(groupPosition), getChildrenCount(groupPosition)));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        KanboardTask child = (KanboardTask) getChild(groupPosition, childPosition);

        if (convertView == null)
            convertView = mInflater.inflate(R.layout.listitem_project_task, null);

        ((TextView) convertView.findViewById(R.id.task_name)).setText(Html.fromHtml(String.format(Locale.getDefault(), "<big><b>#%d</b></big><br />%s", child.getId(), child.getTitle())));

        convertView.findViewById(R.id.task_owner).setVisibility(View.INVISIBLE);

        if (child.getColorBackground() != null)
            convertView.findViewById(R.id.list_card).setBackgroundColor(child.getColorBackground());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
